package com.ruoyi.wvp.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wvp.common.SystemAllInfo;
import com.ruoyi.wvp.common.VersionPo;
import com.ruoyi.wvp.common.enums.ChannelDataType;
import com.ruoyi.wvp.conf.DynamicTask;
import com.ruoyi.wvp.conf.SipConfig;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.conf.VersionInfo;
import com.ruoyi.common.exception.ControllerException;
import com.ruoyi.wvp.gb28181.service.IDeviceChannelService;
import com.ruoyi.wvp.gb28181.service.IDeviceService;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.media.event.mediaServer.MediaServerChangeEvent;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.media.domain.MediaServerLoad;
import com.ruoyi.wvp.storager.IRedisCatchStorage;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyService;
import com.ruoyi.wvp.streamPush.service.IStreamPushService;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.wvp.vmanager.bean.ResourceBaseInfo;
import com.ruoyi.wvp.vmanager.bean.ResourceInfo;
import com.ruoyi.wvp.vmanager.bean.SystemConfigInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 服务控制
 */
@Slf4j
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/server")
public class WvpServerController extends BaseController {


    @Autowired
    private IMediaServerService mediaServerService;

    @Autowired
    private VersionInfo versionInfo;

    @Autowired
    private SipConfig sipConfig;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IDeviceChannelService channelService;

    @Autowired
    private IStreamPushService pushService;

    @Autowired
    private IStreamProxyService proxyService;

    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private IRedisCatchStorage redisCatchStorage;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private DynamicTask dynamicTask;

    /**
     * 获取流媒体服务列表
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wvp:server:list')")
    @GetMapping(value = "/media_server/list")
    @ResponseBody
    public AjaxResult getMediaServerList() {
        return success(mediaServerService.getAll());
    }

    /**
     * 获取流媒体服务列表
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wvp:server:list')")
    @GetMapping(value = "/media_server/online/list")
    @ResponseBody
    public AjaxResult getOnlineMediaServerList() {
        return success(mediaServerService.getAllOnline());
    }

    /**
     * 获取流媒体服务
     *
     * @param id 流媒体服务ID
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wvp:server:query')")
    @GetMapping(value = "/media_server/one/{id}")
    @ResponseBody
    public AjaxResult getMediaServer(@PathVariable String id) {
        // 先从缓存获取
        MediaServer mediaServer = mediaServerService.getOne(id);
        
        // 缓存中没有，再查数据库
        if (mediaServer == null) {
            mediaServer = mediaServerService.getOneFromDatabase(id);
        }
        
        return success(mediaServer);
    }

    /**
     * 检查流媒体服务是否可用
     *
     * @param ip 流媒体服务IP
     * @param httpPort 流媒体服务HTT端口
     * @param secret 流媒体服务secret
     * @param type
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wvp:server:check')")
    @GetMapping(value = "/media_server/check")
    @ResponseBody
    public AjaxResult checkMediaServer(@RequestParam String ip, @RequestParam int httpPort, @RequestParam String secret, @RequestParam String type) throws ControllerException{
        return success(mediaServerService.checkMediaServer(ip, httpPort, secret, type));
    }

    /**
     * 检查流媒体服务是否可用
     *
     * @param ip 流媒体服务IP
     * @param port 流媒体服务HTT端口
     */
    @GetMapping(value = "/media_server/record/check")
    @ResponseBody
    public void checkMediaRecordServer(@RequestParam String ip, @RequestParam int port) {
        boolean checkResult = mediaServerService.checkMediaRecordServer(ip, port);
        if (!checkResult) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "连接失败");
        }
    }

    /**
     * 保存流媒体服务
     *
     * @param mediaServer 流媒体信息
     */
    @PreAuthorize("@ss.hasAnyPermi('wvp:server:add,wvp:server:edit')")
    @PostMapping(value = "/media_server/save")
    @ResponseBody
    public void saveMediaServer(@RequestBody MediaServer mediaServer) {
        MediaServer mediaServerItemInDatabase = mediaServerService.getOneFromDatabase(mediaServer.getId());

        if (mediaServerItemInDatabase != null) {
            // 强制标记为离线，保存后用新IP/端口重新检测
            mediaServer.setStatus(false);
            mediaServerService.update(mediaServer);
            // 取消旧的 keepalive 看门狗，防止它把旧 IP 写回重连队列
            dynamicTask.stop("zlm-keepalive-" + mediaServer.getId());
            // 修改后发布事件，触发立即状态检测（以新的IP/端口重连）
            MediaServerChangeEvent event = new MediaServerChangeEvent(this);
            event.setMediaServerItemList(mediaServer);
            applicationEventPublisher.publishEvent(event);
        } else {
            // 检查数据库中是否已有流媒体，如果为空则设置为默认流媒体
            if (mediaServerService.getAllFromDatabase().isEmpty()) {
                mediaServer.setDefaultServer(true);
            }
            mediaServerService.add(mediaServer);
            // 发送事件
            MediaServerChangeEvent event = new MediaServerChangeEvent(this);
            event.setMediaServerItemList(mediaServer);
            applicationEventPublisher.publishEvent(event);
        }
    }

    /**
     * 删除流媒体服务
     *
     * @param id 流媒体ID
     */
    @PreAuthorize("@ss.hasPermi('wvp:server:delete')")
    @DeleteMapping(value = "/media_server/delete/{id}")
    public AjaxResult deleteMediaServer(@PathVariable String id) {
        // 先从缓存获取
        MediaServer mediaServer = mediaServerService.getOne(id);
        
        // 缓存中没有，再查数据库
        if (mediaServer == null) {
            mediaServer = mediaServerService.getOneFromDatabase(id);
        }
        
        if (mediaServer == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "流媒体不存在");
        }
        mediaServerService.delete(mediaServer);
        return success();
    }

    /**
     * 获取流媒体信息
     *
     * @param app 应用名
     * @param stream 流ID
     * @param mediaServerId 流媒体ID
     * @return
     */
    @GetMapping(value = "/media_server/media_info")
    @ResponseBody
    public AjaxResult getMediaInfo(String app, String stream, String mediaServerId) {
        // 先从缓存获取
        MediaServer mediaServer = mediaServerService.getOne(mediaServerId);
        
        // 缓存中没有，再查数据库
        if (mediaServer == null) {
            mediaServer = mediaServerService.getOneFromDatabase(mediaServerId);
        }
        
        if (mediaServer == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "流媒体不存在");
        }
        return success(mediaServerService.getMediaInfo(mediaServer, app, stream));
    }


    @GetMapping(value = "/restart")
    @ResponseBody
    public void restart() {
//        taskExecutor.execute(()-> {
//            try {
//                Thread.sleep(3000);
//                SipProvider up = (SipProvider) SpringBeanFactory.getBean("udpSipProvider");
//                SipStackImpl stack = (SipStackImpl) up.getSipStack();
//                stack.stop();
//                Iterator listener = stack.getListeningPoints();
//                while (listener.hasNext()) {
//                    stack.deleteListeningPoint((ListeningPoint) listener.next());
//                }
//                Iterator providers = stack.getSipProviders();
//                while (providers.hasNext()) {
//                    stack.deleteSipProvider((SipProvider) providers.next());
//                }
//                VManageBootstrap.restart();
//            } catch (InterruptedException | ObjectInUseException e) {
//                throw new ControllerException(ErrorCode.ERROR100.getCode(), e.getMessage());
//            }
//        });
    }

    /**
     * 获取平台配置信息
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wvp:server:configInfo')")
    @GetMapping(value = "/system/configInfo")
    @ResponseBody
    public AjaxResult getConfigInfo() {
        SystemConfigInfo systemConfigInfo = new SystemConfigInfo();
        systemConfigInfo.setVersion(versionInfo.getVersion());
        systemConfigInfo.setSip(sipConfig);
        systemConfigInfo.setAddOn(userSetting);
        systemConfigInfo.setServerPort(serverPort);
        return success(systemConfigInfo);
    }

    @GetMapping(value = "/version")
    @ResponseBody
    public VersionPo VersionPogetVersion() {
        return versionInfo.getVersion();
    }

    /**
     * 获取平台配置信息
     *
     * @param type 配置类型（sip, base）
     * @return
     */
    @GetMapping(value = "/config")
    @ResponseBody
    public JSONObject getVersion(String type) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("server.port", serverPort);
        if (ObjectUtils.isEmpty(type)) {
            jsonObject.put("sip", JSON.toJSON(sipConfig));
            jsonObject.put("base", JSON.toJSON(userSetting));
        } else {
            switch (type) {
                case "sip":
                    jsonObject.put("sip", sipConfig);
                    break;
                case "base":
                    jsonObject.put("base", userSetting);
                    break;
                default:
                    break;
            }
        }
        return jsonObject;
    }

    @GetMapping(value = "/system/info")
    @ResponseBody
    public AjaxResult getSystemInfo() {
        SystemAllInfo systemAllInfo = redisCatchStorage.getSystemInfo();

        return success(systemAllInfo);
    }

    @GetMapping(value = "/media_server/load")
    @ResponseBody
    public List<MediaServerLoad> getMediaLoad() {
        List<MediaServerLoad> result = new ArrayList<>();
        List<MediaServer> allOnline = mediaServerService.getAllOnline();
        if (allOnline.isEmpty()) {
            return result;
        } else {
            for (MediaServer mediaServerItem : allOnline) {
                result.add(mediaServerService.getLoad(mediaServerItem));
            }
        }
        return result;
    }

    @GetMapping(value = "/resource/info")
    @ResponseBody
    public ResourceInfo getResourceInfo() {
        ResourceInfo result = new ResourceInfo();
        ResourceBaseInfo deviceInfo = deviceService.getOverview();
        result.setDevice(deviceInfo);
        ResourceBaseInfo channelInfo = channelService.getOverview();
        result.setChannel(channelInfo);
        ResourceBaseInfo pushInfo = pushService.getOverview();
        result.setPush(pushInfo);
        ResourceBaseInfo proxyInfo = proxyService.getOverview();
        result.setProxy(proxyInfo);

        return result;
    }

    @GetMapping(value = "/info")
    @ResponseBody
    public Map<String, Map<String, String>> getInfo() {
        Map<String, Map<String, String>> result = new LinkedHashMap<>();
        Map<String, String> hardwareMap = new LinkedHashMap<>();
        result.put("硬件信息", hardwareMap);

        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 获取CPU信息
        hardwareMap.put("CPU", getCpuName());
        // 获取内存
        long totalMem = osBean.getTotalPhysicalMemorySize();
        long freeMem = osBean.getFreePhysicalMemorySize();
        hardwareMap.put("内存", formatByte(totalMem - freeMem) + "/" + formatByte(totalMem));
        // 制造商 / 产品名称（纯 JDK 无法稳定获取，降级展示）
        hardwareMap.put("制造商", getManufacturer());
        hardwareMap.put("产品名称", getModel());
        // 网卡 IP（纯 JDK，不依赖 JNA）
        StringBuilder ips = new StringBuilder();
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            List<String> allIps = new ArrayList<>();
            while (nets.hasMoreElements()) {
                NetworkInterface nif = nets.nextElement();
                if (nif.isLoopback() || !nif.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addrs = nif.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr instanceof Inet4Address) {
                        allIps.add(addr.getHostAddress());
                    }
                }
            }
            ips.append(StringUtils.join(allIps, ","));
        } catch (Exception e) {
            log.error("[获取网卡信息失败] {}", e.getMessage());
        }
        hardwareMap.put("网卡", ips.toString());

        Map<String, String> operatingSystemMap = new LinkedHashMap<>();
        result.put("操作系统", operatingSystemMap);
        operatingSystemMap.put("名称", System.getProperty("os.name") + " " + System.getProperty("os.version"));
        operatingSystemMap.put("类型", System.getProperty("os.arch"));

        Map<String, String> platformMap = new LinkedHashMap<>();
        result.put("平台信息", platformMap);
        VersionPo version = versionInfo.getVersion();
        platformMap.put("版本", version.getVersion());
        platformMap.put("构建日期", version.getBUILD_DATE());
        platformMap.put("GIT分支", version.getGIT_BRANCH());
        platformMap.put("GIT地址", version.getGIT_URL());
        platformMap.put("GIT日期", version.getGIT_DATE());
        platformMap.put("GIT版本", version.getGIT_Revision_SHORT());
        platformMap.put("DOCKER环境", new File("/.dockerenv").exists() ? "是" : "否");

        return result;
    }

    private String getCpuName() {
        try {
            String os = System.getProperty("os.name", "");
            if (os.startsWith("Windows")) {
                return System.getenv("PROCESSOR_IDENTIFIER");
            } else if (os.startsWith("Linux")) {
                for (String line : java.nio.file.Files.readAllLines(java.nio.file.Paths.get("/proc/cpuinfo"))) {
                    if (line.startsWith("model name")) {
                        int idx = line.indexOf(':');
                        if (idx >= 0) {
                            return line.substring(idx + 1).trim();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("[读取 CPU 名称失败] {}", e.getMessage());
        }
        return "Unknown";
    }

    private String getManufacturer() {
        return System.getProperty("os.arch", "Unknown");
    }

    private String getModel() {
        return "Unknown";
    }

    @GetMapping(value = "/channel/datatype")
    @ResponseBody
    public List<Map<String, Object>> getDataType() {
        List<Map<String, Object>> result = new LinkedList<>();
        for (ChannelDataType item : ChannelDataType.values()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("key", item.desc);
            map.put("value", item.value);
            result.add(map);
        }
        return result;
    }

    /**
     * 单位转换
     */
    private static String formatByte(long byteNumber) {
        //换算单位
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }
}
