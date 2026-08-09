package com.ruoyi.wvp.gb28181.transmit.event.request.impl;

import com.ruoyi.wvp.conf.SipConfig;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.gb28181.auth.DigestServerAuthenticationHelper;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.bean.GbSipDate;
import com.ruoyi.wvp.gb28181.bean.RemoteAddressInfo;
import com.ruoyi.wvp.gb28181.bean.SipTransactionInfo;
import com.ruoyi.wvp.gb28181.service.IDeviceService;
import com.ruoyi.wvp.gb28181.transmit.SIPProcessorObserver;
import com.ruoyi.wvp.gb28181.transmit.SIPSender;
import com.ruoyi.wvp.gb28181.transmit.event.request.ISIPRequestProcessor;
import com.ruoyi.wvp.gb28181.transmit.event.request.SIPRequestProcessorParent;
import com.ruoyi.wvp.gb28181.utils.SipUtils;
import com.ruoyi.wvp.service.ISipBlackService;
import com.ruoyi.wvp.utils.DateUtil;
import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.SIPDateHeader;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.sip.RequestEvent;
import javax.sip.SipException;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.UserAgentHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

/**
 * SIP命令类型： REGISTER请求
 */
@Slf4j
@Component
public class RegisterRequestProcessor extends SIPRequestProcessorParent implements InitializingBean, ISIPRequestProcessor {

    public final String method = "REGISTER";

    @Autowired
    private SipConfig sipConfig;

    @Autowired
    private SIPProcessorObserver sipProcessorObserver;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private SIPSender sipSender;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private ISipBlackService sipBlackService;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加消息处理的订阅
        sipProcessorObserver.addRequestProcessor(method, this);
    }

    /**
     * 收到注册请求 处理
     *
     * @param evt
     */
    @Override
    public void process(RequestEvent evt) {
        try {
            SIPRequest request = (SIPRequest) evt.getRequest();
            Response response = null;
            boolean passwordCorrect = false;
            // 注册标志
            boolean registerFlag = true;
            if (request.getExpires().getExpires() == 0) {
                // 注销成功
                registerFlag = false;
            }
            FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);
            AddressImpl address = (AddressImpl) fromHeader.getAddress();
            SipUri uri = (SipUri) address.getURI();
            String deviceId = uri.getUser();

            RemoteAddressInfo remoteAddressInfo = SipUtils.getRemoteAddressFromRequest(request,
                    userSetting.getSipUseSourceIpAsRemoteAddress());
            String requestAddress = remoteAddressInfo.getIp() + ":" + remoteAddressInfo.getPort();
            String title = registerFlag ? "[注册请求]" : "[注销请求]";
            log.info(title + "设备：{}, 开始处理: {}", deviceId, requestAddress);

            // 黑名单检查：设备ID匹配 + IP/端口（若条目中填写了则需要一致）
            if (registerFlag && sipBlackService.isBlacklisted(deviceId,
                    remoteAddressInfo.getIp(), remoteAddressInfo.getPort())) {
                log.warn("{} 设备：{} (IP:{}, 端口:{}) 命中黑名单，拒绝接入",
                        title, deviceId, remoteAddressInfo.getIp(), remoteAddressInfo.getPort());
                response = getMessageFactory().createResponse(Response.FORBIDDEN, request);
                response.setReasonPhrase("Device Blacklisted");
                sipSender.transmitRequest(request.getLocalAddress().getHostAddress(), response);
                return;
            }

            Device device = deviceService.getDeviceByDeviceId(deviceId);

            if (device != null &&
                    device.getSipTransactionInfo() != null &&
                    request.getCallIdHeader().getCallId().equals(device.getSipTransactionInfo().getCallId())) {
                log.info(title + "设备：{}, 注册续订: {}", device.getDeviceId(), device.getDeviceId());
                if (registerFlag) {
                    device.setExpires(request.getExpires().getExpires());
                    device.setIp(remoteAddressInfo.getIp());
                    device.setPort(remoteAddressInfo.getPort());
                    device.setHostAddress(remoteAddressInfo.getIp().concat(":").concat(String.valueOf(remoteAddressInfo.getPort())));

                    device.setLocalIp(request.getLocalAddress().getHostAddress());
                    Response registerOkResponse = getRegisterOkResponse(request);
                    // 判断TCP还是UDP
                    ViaHeader reqViaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
                    String transport = reqViaHeader.getTransport();
                    device.setTransport("TCP".equalsIgnoreCase(transport) ? "TCP" : "UDP");
                    sipSender.transmitRequest(request.getLocalAddress().getHostAddress(), registerOkResponse);
                    device.setRegisterTime(DateUtil.getNow());
                    SipTransactionInfo sipTransactionInfo = new SipTransactionInfo((SIPResponse) registerOkResponse);
                    deviceService.online(device, sipTransactionInfo);
                } else {
                    deviceService.offline(deviceId, "主动注销");
                }
                return;
            }
            // 检测设备支持的GB28181协议版本
            int detectedVersion = detectProtocolVersion(request);
            log.info(title + " 设备：{}, 检测到协议版本: GB28181-{}", deviceId, detectedVersion == 2 ? "2022" : "2016");

            String password = (device != null && !ObjectUtils.isEmpty(device.getPassword())) ? device.getPassword() : sipConfig.getPassword();
            AuthorizationHeader authHead = (AuthorizationHeader) request.getHeader(AuthorizationHeader.NAME);
            if (authHead == null && !ObjectUtils.isEmpty(password)) {
                log.info(title + " 设备：{}, 回复401: {}", deviceId, requestAddress);
                response = getMessageFactory().createResponse(Response.UNAUTHORIZED, request);
                // 统一使用MD5（兼容GB28181-2022设备，大部分设备厂商仍未实现SHA-256）
                new DigestServerAuthenticationHelper(DigestServerAuthenticationHelper.ALGORITHM_MD5)
                        .generateChallenge(getHeaderFactory(), response, sipConfig.getDomain());
                sipSender.transmitRequest(request.getLocalAddress().getHostAddress(), response);
                return;
            }

            // 校验密码是否正确（统一使用MD5，兼容GB28181-2022设备）
            passwordCorrect = ObjectUtils.isEmpty(password) ||
                    new DigestServerAuthenticationHelper(DigestServerAuthenticationHelper.ALGORITHM_MD5)
                            .doAuthenticatePlainTextPassword(request, password);

            if (!passwordCorrect) {
                // 注册失败
                response = getMessageFactory().createResponse(Response.FORBIDDEN, request);
                response.setReasonPhrase("wrong password");
                log.info(title + " 设备：{}, 密码/SIP服务器ID错误, 回复403: {}", deviceId, requestAddress);
                sipSender.transmitRequest(request.getLocalAddress().getHostAddress(), response);
                return;
            }

            // 携带授权头并且密码正确
            response = getMessageFactory().createResponse(Response.OK, request);
            // 添加date头
            SIPDateHeader dateHeader = new SIPDateHeader();
            // 使用自己修改的
            GbSipDate gbSipDate = new GbSipDate(Calendar.getInstance(Locale.ENGLISH).getTimeInMillis());
            dateHeader.setDate(gbSipDate);
            response.addHeader(dateHeader);

            if (request.getExpires() == null) {
                response = getMessageFactory().createResponse(Response.BAD_REQUEST, request);
                sipSender.transmitRequest(request.getLocalAddress().getHostAddress(), response);
                return;
            }
            // 添加Contact头
            response.addHeader(request.getHeader(ContactHeader.NAME));
            // 添加Expires头
            response.addHeader(request.getExpires());

            if (device == null) {
                device = new Device();
                device.setStreamMode("TCP-PASSIVE");
                // GB28181-2022标准强制GB18030字符集，2016版默认GB2312
                device.setCharset(detectedVersion == 2 ? "GB18030" : "GB2312");
                device.setGeoCoordSys("WGS84");
                device.setMediaServerId("auto");
                device.setDeviceId(deviceId);
                device.setOnLine(false);
                device.setProtocolVersion(detectedVersion);
            } else {
                if (ObjectUtils.isEmpty(device.getStreamMode())) {
                    device.setStreamMode("TCP-PASSIVE");
                }
                if (ObjectUtils.isEmpty(device.getCharset())) {
                    device.setCharset(detectedVersion == 2 ? "GB18030" : "GB2312");
                }
                if (ObjectUtils.isEmpty(device.getGeoCoordSys())) {
                    device.setGeoCoordSys("WGS84");
                }
                // 已注册设备更新协议版本（检测到的版本优先）
                device.setProtocolVersion(detectedVersion);
            }

            device.setIp(remoteAddressInfo.getIp());
            device.setPort(remoteAddressInfo.getPort());
            device.setHostAddress(remoteAddressInfo.getIp().concat(":").concat(String.valueOf(remoteAddressInfo.getPort())));
            device.setLocalIp(request.getLocalAddress().getHostAddress());
            if (request.getExpires().getExpires() == 0) {
                // 注销成功
                registerFlag = false;
            } else {
                // 注册成功
                device.setExpires(request.getExpires().getExpires());
                registerFlag = true;
                // 判断TCP还是UDP
                ViaHeader reqViaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
                String transport = reqViaHeader.getTransport();
                device.setTransport("TCP".equalsIgnoreCase(transport) ? "TCP" : "UDP");
            }

            sipSender.transmitRequest(request.getLocalAddress().getHostAddress(), response);
            // 注册成功
            // 保存到redis
            if (registerFlag) {
                log.info("[注册成功] deviceId: {}->{}", deviceId, requestAddress);
                device.setRegisterTime(DateUtil.getNow());
                SipTransactionInfo sipTransactionInfo = new SipTransactionInfo((SIPResponse) response);
                deviceService.online(device, sipTransactionInfo);
            } else {
                log.info("[注销成功] deviceId: {}->{}", deviceId, requestAddress);
                deviceService.offline(deviceId, "主动注销");
            }
        } catch (SipException | NoSuchAlgorithmException | ParseException e) {
            log.error("未处理的异常 ", e);
        }
    }

    /**
     * 检测设备支持的 GB28181 协议版本
     * 检测途径（按优先级）:
     * 1. X-GB-Ver 自定义SIP头（参考 wvp 兼容实现，3.0=2022版，2.0=2016版）
     * 2. User-Agent 头中的版本标识
     * 3. Contact 头中的扩展参数
     * 4. 默认 2016（兼容未知设备）
     */
    private int detectProtocolVersion(SIPRequest request) {
        // 1. X-GB-Ver 头（2022版设备显式声明）
        javax.sip.header.Header verHeader = request.getHeader("X-GB-Ver");
        if (verHeader != null) {
            String verValue = verHeader.toString().trim();
            int idx = verValue.indexOf(':');
            if (idx >= 0) {
                String ver = verValue.substring(idx + 1).trim();
                if ("3.0".equals(ver) || "2022".equals(ver)) {
                    log.info("检测到 X-GB-Ver 头: {}, 判定为GB28181-2022设备", ver);
                    return 2;
                }
            }
        }

        // 2. User-Agent 头
        UserAgentHeader userAgentHeader = (UserAgentHeader) request.getHeader(UserAgentHeader.NAME);
        if (userAgentHeader != null) {
            String ua = userAgentHeader.toString().toLowerCase();
            if (ua.contains("gb28181-2022") || ua.contains("gb/t 28181-2022") || ua.contains("gb28181 2022")) {
                return 2;
            }
        }

        // 3. Contact 头中的扩展参数
        ContactHeader contactHeader = (ContactHeader) request.getHeader(ContactHeader.NAME);
        if (contactHeader != null && contactHeader.toString().contains("version=2")) {
            return 2;
        }

        // 4. 默认 2016
        return 1;
    }

    private Response getRegisterOkResponse(Request request) throws ParseException {
        // 携带授权头并且密码正确
        Response response = getMessageFactory().createResponse(Response.OK, request);
        // 添加date头
        SIPDateHeader dateHeader = new SIPDateHeader();
        // 使用自己修改的
        GbSipDate gbSipDate = new GbSipDate(Calendar.getInstance(Locale.ENGLISH).getTimeInMillis());
        dateHeader.setDate(gbSipDate);
        response.addHeader(dateHeader);

        // 添加Contact头
        response.addHeader(request.getHeader(ContactHeader.NAME));
        // 添加Expires头
        response.addHeader(request.getExpires());

        return response;

    }
}
