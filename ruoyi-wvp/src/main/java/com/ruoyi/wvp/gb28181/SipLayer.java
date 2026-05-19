package com.ruoyi.wvp.gb28181;


import com.ruoyi.wvp.conf.SipConfig;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.gb28181.bean.GbStringMsgParserFactory;
import com.ruoyi.wvp.gb28181.conf.DefaultProperties;
import com.ruoyi.wvp.gb28181.transmit.ISIPProcessorObserver;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.SipStackImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.sip.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@Order(value=10)
public class SipLayer implements CommandLineRunner {

	@Autowired
	private SipConfig sipConfig;

	@Autowired
	private ISIPProcessorObserver sipProcessorObserver;

	@Autowired
	private UserSetting userSetting;

	private final Map<String, SipProviderImpl> tcpSipProviderMap = new ConcurrentHashMap<>();
	private final Map<String, SipProviderImpl> udpSipProviderMap = new ConcurrentHashMap<>();
	private final List<String> monitorIps = new ArrayList<>();

	@Override
	public void run(String... args) {
		try {
			if (ObjectUtils.isEmpty(sipConfig.getIp())) {
				try {
					// 获得本机的所有网络接口
					Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
					while (nifs.hasMoreElements()) {
						NetworkInterface nif = nifs.nextElement();
						// 获得与该网络接口绑定的 IP 地址，一般只有一个
						Enumeration<InetAddress> addresses = nif.getInetAddresses();
						while (addresses.hasMoreElements()) {
							InetAddress addr = addresses.nextElement();
							if (addr instanceof Inet4Address) {
								if (addr.getHostAddress().equals("127.0.0.1")){
									continue;
								}
								if (nif.getName().startsWith("docker")) {
									continue;
								}
								log.info("[自动配置SIP监听网卡] 网卡接口地址： {}", addr.getHostAddress());// 只关心 IPv4 地址
								monitorIps.add(addr.getHostAddress());
							}
						}
					}
				}catch (Exception e) {
					log.error("[读取网卡信息失败]", e);
				}
				if (monitorIps.isEmpty()) {
					log.error("[自动配置SIP监听网卡信息失败]，请手动配置SIP.IP后重新启动，但应用将继续运行其他功能");
					// 不再强制退出，允许应用继续运行
					return;
				}
			}else {
				// 使用逗号分割多个ip
				String separator = ",";
				if (sipConfig.getIp().indexOf(separator) > 0) {
					String[] split = sipConfig.getIp().split(separator);
					monitorIps.addAll(Arrays.asList(split));
				}else {
					monitorIps.add(sipConfig.getIp());
				}
			}
			if (ObjectUtils.isEmpty(sipConfig.getShowIp())){
				sipConfig.setShowIp(String.join(",", monitorIps));
			}
			SipFactory.getInstance().setPathName("gov.nist");
			if (monitorIps.size() > 0) {
				for (String monitorIp : monitorIps) {
					addListeningPoint(monitorIp, sipConfig.getPort());
				}
				if (udpSipProviderMap.size() + tcpSipProviderMap.size() == 0) {
					log.error("[SIP SERVER] 所有SIP监听点启动失败，但应用将继续运行其他功能。请检查配置后重启SIP服务。");
					// 不再强制退出，允许应用继续运行
					return;
				}
				log.info("[SIP SERVER] SIP服务初始化完成，成功启动 {} 个UDP监听点和 {} 个TCP监听点",
						udpSipProviderMap.size(), tcpSipProviderMap.size());
			}
		} catch (Exception e) {
			log.error("[SIP SERVER] SIP服务初始化过程中发生异常，但应用将继续运行其他功能", e);
			// 捕获所有未预期的异常，确保不会影响应用启动
		}
	}

	private void addListeningPoint(String monitorIp, int port){
		SipStackImpl sipStack;
		try {
			sipStack = (SipStackImpl)SipFactory.getInstance().createSipStack(DefaultProperties.getProperties("GB28181_SIP", userSetting.getSipLog()));
			sipStack.setMessageParserFactory(new GbStringMsgParserFactory());
		} catch (PeerUnavailableException e) {
			log.error("[SIP SERVER] SIP服务启动失败，监听地址{}失败，请检查ip是否正确", monitorIp, e);
			return;
		}

		try {
			ListeningPoint tcpListeningPoint = sipStack.createListeningPoint(monitorIp, port, "TCP");
			SipProviderImpl tcpSipProvider = (SipProviderImpl)sipStack.createSipProvider(tcpListeningPoint);

			tcpSipProvider.setDialogErrorsAutomaticallyHandled();
			tcpSipProvider.addSipListener(sipProcessorObserver);
			tcpSipProviderMap.put(monitorIp, tcpSipProvider);
			log.info("[SIP SERVER] tcp://{}:{} 启动成功", monitorIp, port);
		} catch (TransportNotSupportedException
				 | TooManyListenersException
				 | ObjectInUseException
				 | InvalidArgumentException e) {
			log.error("[SIP SERVER] tcp://{}:{} SIP服务启动失败，请检查端口是否被占用或者ip是否正确"
					, monitorIp, port, e);
		}

		try {
			ListeningPoint udpListeningPoint = sipStack.createListeningPoint(monitorIp, port, "UDP");

			SipProviderImpl udpSipProvider = (SipProviderImpl)sipStack.createSipProvider(udpListeningPoint);
			udpSipProvider.addSipListener(sipProcessorObserver);
			udpSipProvider.setDialogErrorsAutomaticallyHandled();
			udpSipProviderMap.put(monitorIp, udpSipProvider);

			log.info("[SIP SERVER] udp://{}:{} 启动成功", monitorIp, port);
		} catch (TransportNotSupportedException
				 | TooManyListenersException
				 | ObjectInUseException
				 | InvalidArgumentException e) {
			log.error("[SIP SERVER] udp://{}:{} SIP服务启动失败，请检查端口是否被占用或者ip是否正确"
					, monitorIp, port, e);
		}
	}

	public SipProviderImpl getUdpSipProvider(String ip) {
		if (udpSipProviderMap.size() == 1) {
			return udpSipProviderMap.values().stream().findFirst().get();
		}
		if (ObjectUtils.isEmpty(ip)) {
			return null;
		}
		return udpSipProviderMap.get(ip);
	}

	public SipProviderImpl getUdpSipProvider() {
		if (udpSipProviderMap.size() != 1) {
			return null;
		}
		return udpSipProviderMap.values().stream().findFirst().get();
	}

	public SipProviderImpl getTcpSipProvider() {
		if (tcpSipProviderMap.size() != 1) {
			return null;
		}
		return tcpSipProviderMap.values().stream().findFirst().get();
	}

	public SipProviderImpl getTcpSipProvider(String ip) {
		if (tcpSipProviderMap.size() == 1) {
			return tcpSipProviderMap.values().stream().findFirst().get();
		}
		if (ObjectUtils.isEmpty(ip)) {
			return null;
		}
		return tcpSipProviderMap.get(ip);
	}

	public String getLocalIp(String deviceLocalIp) {
		if (monitorIps.size() == 1) {
			return monitorIps.get(0);
		}
		if (!ObjectUtils.isEmpty(deviceLocalIp)) {
			return deviceLocalIp;
		}
		SipProviderImpl provider = getUdpSipProvider();
		if (provider != null) {
			return provider.getListeningPoint().getIPAddress();
		}
		// 如果SIP服务未启动，尝试从配置的IP中获取
		if (!ObjectUtils.isEmpty(sipConfig.getIp())) {
			String[] ips = sipConfig.getIp().split(",");
			if (ips.length > 0) {
				log.warn("[SIP SERVER] SIP服务未启动，使用配置的IP: {}", ips[0]);
				return ips[0];
			}
		}
		// 最后的兜底方案：返回本地回环地址
		log.error("[SIP SERVER] SIP服务未启动且无法获取有效IP，返回默认地址127.0.0.1");
		return "127.0.0.1";
	}
}
