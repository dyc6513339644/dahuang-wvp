package com.ruoyi.onvif.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.ruoyi.onvif.exception.OnvifException;
import lombok.extern.slf4j.Slf4j;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * ONVIF 工具类
 */
@Slf4j
public final class OnvifUtils {

	private static final String NS_S = "http://www.w3.org/2003/05/soap-envelope";
	private static final String NS_PTZ = "http://www.onvif.org/ver20/ptz/wsdl";
	private static final String NS_MEDIA = "http://www.onvif.org/ver10/media/wsdl";
	private static final String NS_TT = "http://www.onvif.org/ver10/schema";
	private static final String NS_IMG = "http://www.onvif.org/ver20/imaging/wsdl";
	private static final String NS_DEVICE = "http://www.onvif.org/ver10/device/wsdl";

	private static final int DEFAULT_TIMEOUT_MS = 5000;

	/** WS-Discovery 组播地址及端口 */
	private static final String WS_DISCOVERY_ADDR = "239.255.255.250";
	private static final int WS_DISCOVERY_PORT = 3702;

	private OnvifUtils() {}

	private static String baseUrl(String host, int port) {
		return "http://" + host + ":" + port;
	}

	// ============================== Device ==============================

	/**
	 * 获取设备基本信息：厂商、型号、固件版本、序列号、硬件ID。
	 */
	public static DeviceInformation getDeviceInformation(String host, int port,
			String username, String password) {
		String url = baseUrl(host, port) + "/onvif/device_service";
		String body = "<tds:GetDeviceInformation xmlns:tds=\"" + NS_DEVICE + "\"/>";
		String resp = sendSoap(url, NS_DEVICE + "/GetDeviceInformation", body, username, password, host, port);
		String manufacturer = ReUtil.getGroup1("<[^>]*Manufacturer[^>]*>([^<]+)</[^>]*Manufacturer>", resp);
		String model = ReUtil.getGroup1("<[^>]*Model[^>]*>([^<]+)</[^>]*Model>", resp);
		String firmwareVersion = ReUtil.getGroup1("<[^>]*FirmwareVersion[^>]*>([^<]+)</[^>]*FirmwareVersion>", resp);
		String serialNumber = ReUtil.getGroup1("<[^>]*SerialNumber[^>]*>([^<]+)</[^>]*SerialNumber>", resp);
		String hardwareId = ReUtil.getGroup1("<[^>]*HardwareId[^>]*>([^<]+)</[^>]*HardwareId>", resp);
		return new DeviceInformation(manufacturer, model, firmwareVersion, serialNumber, hardwareId);
	}

	// ============================== WS-Discovery ==============================

	/**
	 * WS-Discovery 设备发现：通过 UDP 组播搜索局域网内 ONVIF 设备。
	 * <p>
	 * 发送 Probe 消息到 {@code 239.255.255.250:3702}，等待设备响应。
	 *
	 * @param timeoutSeconds 等待响应的超时秒数（建议 2~5 秒）
	 * @return 发现的设备列表，含地址、XAddrs、名称、型号等信息
	 */
	public static List<DiscoveryInfo> wsDiscovery(int timeoutSeconds) {
		List<DiscoveryInfo> result = new ArrayList<>();
		String probeMsg = buildProbeMessage();
		byte[] sendData = probeMsg.getBytes(StandardCharsets.UTF_8);

		try (DatagramSocket socket = new DatagramSocket()) {
			socket.setSoTimeout(timeoutSeconds * 1000);
			InetAddress group = InetAddress.getByName(WS_DISCOVERY_ADDR);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, group, WS_DISCOVERY_PORT);
			socket.send(sendPacket);
			log.debug("[WS-Discovery] Probe sent to {}:{}", WS_DISCOVERY_ADDR, WS_DISCOVERY_PORT);

			byte[] recvBuf = new byte[65535];
			long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
			while (System.currentTimeMillis() < deadline) {
				try {
					DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
					socket.receive(recvPacket);
					String response = new String(recvPacket.getData(), 0, recvPacket.getLength(), StandardCharsets.UTF_8);
					log.debug("[WS-Discovery] <= {} bytes from {}", response.length(), recvPacket.getAddress().getHostAddress());
					List<DiscoveryInfo> matches = parseProbeMatches(response);
					result.addAll(matches);
				} catch (java.net.SocketTimeoutException e) {
					break;
				}
			}
		} catch (Exception e) {
			log.warn("[WS-Discovery] 发现失败: {}", e.getMessage());
		}
		return result;
	}

	/** 默认 3 秒超时。 */
	public static List<DiscoveryInfo> wsDiscovery() {
		return wsDiscovery(3);
	}

	private static String buildProbeMessage() {
		String messageId = "urn:uuid:" + IdUtil.simpleUUID();
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<e:Envelope xmlns:e=\"http://www.w3.org/2003/05/soap-envelope\""
			+ " xmlns:w=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\""
			+ " xmlns:d=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\""
			+ " xmlns:dn=\"http://www.onvif.org/ver10/network/wsdl\">"
			+ "<e:Header>"
			+ "<w:MessageID>" + messageId + "</w:MessageID>"
			+ "<w:To>urn:schemas-xmlsoap-org:ws:2005:04:discovery</w:To>"
			+ "<w:Action>http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</w:Action>"
			+ "</e:Header>"
			+ "<e:Body>"
			+ "<d:Probe>"
			+ "<d:Types>dn:NetworkVideoTransmitter</d:Types>"
			+ "</d:Probe>"
			+ "</e:Body>"
			+ "</e:Envelope>";
	}

	private static List<DiscoveryInfo> parseProbeMatches(String xml) {
		List<DiscoveryInfo> result = new ArrayList<>();
		String[] blocks = xml.split("<[^:]*:ProbeMatch");
		for (int i = 1; i < blocks.length; i++) {
			String block = blocks[i];
			String xAddrs = ReUtil.getGroup1("<[^:]*:XAddrs[^>]*>([^<]+)</[^:]*:XAddrs>", block);
			if (StrUtil.isBlank(xAddrs)) continue;
			// 取首个地址
			String firstXAddr = xAddrs.split("\\s+")[0].trim();
			// 提取 IP
			String ip = ReUtil.getGroup1("://([^:/]+)", firstXAddr);
			// 从 scopes 提取 hostName
			String scopes = ReUtil.getGroup1("<[^:]*:Scopes[^>]*>([^<]+)</[^:]*:Scopes>", block);
			String hostName = ReUtil.getGroup1("onvif://www\\.onvif\\.org/name/([^\\s]+)", scopes != null ? scopes : "");
			result.add(new DiscoveryInfo(ip, hostName));
		}
		return result;
	}

	// ============================== Channel ==============================

	/**
	 * 获取所有通道名称与 token（结构化解析）。
	 *
	 * @return 通道信息列表，每条包含 name 和 token
	 */
	public static List<ProfileInfo> getProfiles(String host, int port,
			String username, String password) {
		String url = baseUrl(host, port) + "/onvif/Media";
		String body = "<trt:GetProfiles xmlns:trt=\"" + NS_MEDIA + "\"/>";
		String resp = sendSoap(url, NS_MEDIA + "/GetProfiles", body, username, password, host, port);
		String[] blocks = resp.split("<[^:]*:Profiles\\s");
		List<ProfileInfo> result = new ArrayList<>();
		for (int i = 1; i < blocks.length; i++) {
			String block = blocks[i];
			String token = ReUtil.getGroup1("token=\"([^\"]+)\"", block);
			String name = ReUtil.getGroup1("<[^:]*:Name[^>]*>([^<]+)</[^:]*:Name>", block);
			if (StrUtil.isNotBlank(token) && StrUtil.isNotBlank(name)) {
				result.add(new ProfileInfo(token, name));
			}
		}
		return result;
	}

	/** 获取所有通道原始 XML（含编码参数、分辨率等）。 */
	public static String getProfilesRaw(String host, int port,
			String username, String password) {
		String url = baseUrl(host, port) + "/onvif/Media";
		String body = "<trt:GetProfiles xmlns:trt=\"" + NS_MEDIA + "\"/>";
		return sendSoap(url, NS_MEDIA + "/GetProfiles", body, username, password, host, port);
	}

	/** 获取单个通道的详细信息（Profile 完整 XML）。 */
	public static String getProfile(String host, int port,
			String username, String password, String profileToken) {
		String url = baseUrl(host, port) + "/onvif/Media";
		String body = "<trt:GetProfile xmlns:trt=\"" + NS_MEDIA + "\">"
			+ "<trt:ProfileToken>" + profileToken + "</trt:ProfileToken>"
			+ "</trt:GetProfile>";
		return sendSoap(url, NS_MEDIA + "/GetProfile", body, username, password, host, port);
	}

	/**
	 * 获取 RTSP 取流地址。
	 *
	 * @return RTSP URL，例如 {@code rtsp://192.168.1.7:554/Streaming/Channels/101}
	 */
	public static String getStreamUri(String host, int port,
			String username, String password, String profileToken) {
		String url = baseUrl(host, port) + "/onvif/Media";
		String body = "<trt:GetStreamUri xmlns:trt=\"" + NS_MEDIA + "\" xmlns:tt=\"" + NS_TT + "\">"
			+ "<trt:StreamSetup>"
			+ "<tt:Stream>RTP-Unicast</tt:Stream>"
			+ "<tt:Transport><tt:Protocol>RTSP</tt:Protocol></tt:Transport>"
			+ "</trt:StreamSetup>"
			+ "<trt:ProfileToken>" + profileToken + "</trt:ProfileToken>"
			+ "</trt:GetStreamUri>";
		String resp = sendSoap(url, NS_MEDIA + "/GetStreamUri", body, username, password, host, port);
		return ReUtil.getGroup1("<[^>]*Uri[^>]*>([^<]+)</[^>]*Uri>", resp);
	}

	// ============================== Token ==============================

	/** 获取首个 ProfileToken（PTZ 用），每次实时请求。 */
	public static String getProfileToken(String host, int port,
			String username, String password) {
		String url = baseUrl(host, port) + "/onvif/Media";
		String body = "<trt:GetProfiles xmlns:trt=\"" + NS_MEDIA + "\"/>";
		String resp = sendSoap(url, NS_MEDIA + "/GetProfiles", body, username, password, host, port);
		String token = ReUtil.getGroup1("Profiles[^>]*\\stoken=\"([^\"]+)\"", resp);
		if (StrUtil.isBlank(token)) token = ReUtil.getGroup1("token=\"([^\"]+)\"", resp);
		if (StrUtil.isBlank(token)) {
			throw new OnvifException("无法获取 ONVIF ProfileToken，响应: " + StrUtil.maxLength(resp, 500));
		}
		return token;
	}

	/** 获取首个 VideoSourceToken（Imaging 用），每次实时请求。 */
	public static String getVideoSourceToken(String host, int port,
			String username, String password) {
		String url = baseUrl(host, port) + "/onvif/Media";
		String body = "<trt:GetVideoSources xmlns:trt=\"" + NS_MEDIA + "\"/>";
		String resp = sendSoap(url, NS_MEDIA + "/GetVideoSources", body, username, password, host, port);
		String token = ReUtil.getGroup1("VideoSources[^>]*\\stoken=\"([^\"]+)\"", resp);
		if (StrUtil.isBlank(token)) token = ReUtil.getGroup1("token=\"([^\"]+)\"", resp);
		if (StrUtil.isBlank(token)) {
			throw new OnvifException("无法获取 ONVIF VideoSourceToken，响应: " + StrUtil.maxLength(resp, 500));
		}
		return token;
	}

	// ============================== PTZ ==============================

	/** 连续移动（pan/tilt/zoom 同时控制，需后续调 ptzStop）。范围 -1.0~1.0。 */
	public static void ptzContinuousMove(String host, int port,
			String username, String password,
			String profileToken, double pan, double tilt, double zoom) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:ContinuousMove xmlns:tptz=\"" + NS_PTZ + "\" xmlns:tt=\"" + NS_TT + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "<tptz:Velocity>"
			+ "<tt:PanTilt x=\"" + pan + "\" y=\"" + tilt + "\"/>"
			+ "<tt:Zoom x=\"" + zoom + "\"/>"
			+ "</tptz:Velocity>"
			+ "</tptz:ContinuousMove>";
		sendSoap(url, NS_PTZ + "/ContinuousMove", body, username, password, host, port);
	}

	/** 停止云台 + 变倍。 */
	public static void ptzStop(String host, int port,
			String username, String password, String profileToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:Stop xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "<tptz:PanTilt>true</tptz:PanTilt>"
			+ "<tptz:Zoom>true</tptz:Zoom>"
			+ "</tptz:Stop>";
		sendSoap(url, NS_PTZ + "/Stop", body, username, password, host, port);
	}

	/**
	 * 方向移动快捷方法。
	 *
	 * @param direction 方向：up / down / left / right / zoomin / zoomout
	 * @param speed 速度 0.0~1.0
	 */
	public static void ptzMove(String host, int port,
			String username, String password,
			String profileToken, String direction, double speed) {
		double s = Math.abs(speed);
		switch (direction.toLowerCase()) {
			case "up":     ptzContinuousMove(host, port, username, password, profileToken, 0, s, 0); break;
			case "down":   ptzContinuousMove(host, port, username, password, profileToken, 0, -s, 0); break;
			case "left":   ptzContinuousMove(host, port, username, password, profileToken, -s, 0, 0); break;
			case "right":  ptzContinuousMove(host, port, username, password, profileToken, s, 0, 0); break;
			case "zoomin": ptzContinuousMove(host, port, username, password, profileToken, 0, 0, s); break;
			case "zoomout":ptzContinuousMove(host, port, username, password, profileToken, 0, 0, -s); break;
			default: throw new OnvifException("不支持的方向: " + direction + "，可选: up/down/left/right/zoomin/zoomout");
		}
	}

	/** 相对移动（按当前位置偏移，自动停）。 */
	public static void relativeMove(String host, int port,
			String username, String password,
			String profileToken, float pan, float tilt, float zoom) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:RelativeMove xmlns:tptz=\"" + NS_PTZ + "\" xmlns:tt=\"" + NS_TT + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "<tptz:Translation>"
			+ "<tt:PanTilt x=\"" + pan + "\" y=\"" + tilt + "\"/>"
			+ "<tt:Zoom x=\"" + zoom + "\"/>"
			+ "</tptz:Translation>"
			+ "</tptz:RelativeMove>";
		sendSoap(url, NS_PTZ + "/RelativeMove", body, username, password, host, port);
	}

	/** 绝对移动（到指定坐标）。 */
	public static void absoluteMove(String host, int port,
			String username, String password,
			String profileToken, float pan, float tilt, float zoom) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:AbsoluteMove xmlns:tptz=\"" + NS_PTZ + "\" xmlns:tt=\"" + NS_TT + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "<tptz:Position>"
			+ "<tt:PanTilt x=\"" + pan + "\" y=\"" + tilt + "\"/>"
			+ "<tt:Zoom x=\"" + zoom + "\"/>"
			+ "</tptz:Position>"
			+ "</tptz:AbsoluteMove>";
		sendSoap(url, NS_PTZ + "/AbsoluteMove", body, username, password, host, port);
	}

	// ============================== 预置位 ==============================

	/** 设置预置位（presetToken 为空表示新建，返回设备分配的 token）。 */
	public static String setPreset(String host, int port,
			String username, String password,
			String profileToken, String presetName, String presetToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String tokenXml = StrUtil.isBlank(presetToken) ? "" :
			"<tptz:PresetToken>" + presetToken + "</tptz:PresetToken>";
		String body = "<tptz:SetPreset xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "<tptz:PresetName>" + presetName + "</tptz:PresetName>"
			+ tokenXml
			+ "</tptz:SetPreset>";
		String resp = sendSoap(url, NS_PTZ + "/SetPreset", body, username, password, host, port);
		return ReUtil.getGroup1("<[^>]*PresetToken[^>]*>([^<]+)</[^>]*PresetToken>", resp);
	}

	public static void gotoPreset(String host, int port,
			String username, String password,
			String profileToken, String presetToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:GotoPreset xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "<tptz:PresetToken>" + presetToken + "</tptz:PresetToken>"
			+ "</tptz:GotoPreset>";
		sendSoap(url, NS_PTZ + "/GotoPreset", body, username, password, host, port);
	}

	public static void removePreset(String host, int port,
			String username, String password,
			String profileToken, String presetToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:RemovePreset xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "<tptz:PresetToken>" + presetToken + "</tptz:PresetToken>"
			+ "</tptz:RemovePreset>";
		sendSoap(url, NS_PTZ + "/RemovePreset", body, username, password, host, port);
	}

	public static void gotoHome(String host, int port,
			String username, String password, String profileToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:GotoHomePosition xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "</tptz:GotoHomePosition>";
		sendSoap(url, NS_PTZ + "/GotoHomePosition", body, username, password, host, port);
	}

	public static void setHome(String host, int port,
			String username, String password, String profileToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:SetHomePosition xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "</tptz:SetHomePosition>";
		sendSoap(url, NS_PTZ + "/SetHomePosition", body, username, password, host, port);
	}

	/** 获取所有预置位（结构化解析：token + 名称）。 */
	public static List<PresetInfo> getPresets(String host, int port,
			String username, String password, String profileToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:GetPresets xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "</tptz:GetPresets>";
		String resp = sendSoap(url, NS_PTZ + "/GetPresets", body, username, password, host, port);
		String[] blocks = resp.split("<[^:]*:Preset\\s");
		List<PresetInfo> result = new ArrayList<>();
		for (int i = 1; i < blocks.length; i++) {
			String block = blocks[i];
			String token = ReUtil.getGroup1("token=\"([^\"]+)\"", block);
			String name = ReUtil.getGroup1("<[^:]*:Name[^>]*>([^<]+)</[^:]*:Name>", block);
			if (StrUtil.isNotBlank(token)) {
				result.add(new PresetInfo(token, name));
			}
		}
		return result;
	}

	/** 列出所有预置位（原始 XML）。 */
	public static String listPresets(String host, int port,
			String username, String password, String profileToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:GetPresets xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "</tptz:GetPresets>";
		return sendSoap(url, NS_PTZ + "/GetPresets", body, username, password, host, port);
	}

	// ============================== 状态 ==============================

	/** 获取 PTZ 节点信息（含设备支持的辅助命令列表）。 */
	public static String getNodes(String host, int port,
			String username, String password) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:GetNodes xmlns:tptz=\"" + NS_PTZ + "\"/>";
		return sendSoap(url, NS_PTZ + "/GetNodes", body, username, password, host, port);
	}

	/** 获取 PTZ 状态（当前 pan/tilt/zoom）。 */
	public static String getStatus(String host, int port,
			String username, String password, String profileToken) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:GetStatus xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "</tptz:GetStatus>";
		return sendSoap(url, NS_PTZ + "/GetStatus", body, username, password, host, port);
	}

	// ============================== 聚焦 ==============================

	/** 连续聚焦（speed&gt;0 远焦、&lt;0 近焦），必须配对调用 {@link #focusStop}。 */
	public static void focusMove(String host, int port,
			String username, String password,
			String videoSourceToken, float speed) {
		String url = baseUrl(host, port) + "/onvif/Imaging";
		String body = "<timg:Move xmlns:timg=\"" + NS_IMG + "\" xmlns:tt=\"" + NS_TT + "\">"
			+ "<timg:VideoSourceToken>" + videoSourceToken + "</timg:VideoSourceToken>"
			+ "<timg:Focus>"
			+ "<tt:Continuous><tt:Speed>" + speed + "</tt:Speed></tt:Continuous>"
			+ "</timg:Focus>"
			+ "</timg:Move>";
		sendSoap(url, NS_IMG + "/Move", body, username, password, host, port);
	}

	public static void focusStop(String host, int port,
			String username, String password, String videoSourceToken) {
		String url = baseUrl(host, port) + "/onvif/Imaging";
		String body = "<timg:Stop xmlns:timg=\"" + NS_IMG + "\">"
			+ "<timg:VideoSourceToken>" + videoSourceToken + "</timg:VideoSourceToken>"
			+ "</timg:Stop>";
		sendSoap(url, NS_IMG + "/Stop", body, username, password, host, port);
	}

	// ============================== 光圈 ==============================

	/**
	 * 光圈调节（绝对值 0.0~1.0）。
	 * <p>
	 * 实测大华机型仅接受最小请求体：Exposure.Mode=MANUAL + Exposure.Iris。
	 */
	public static void irisSet(String host, int port,
			String username, String password,
			String videoSourceToken, float irisValue) {
		String url = baseUrl(host, port) + "/onvif/Imaging";
		String body = "<timg:SetImagingSettings xmlns:timg=\"" + NS_IMG + "\" xmlns:tt=\"" + NS_TT + "\">"
			+ "<timg:VideoSourceToken>" + videoSourceToken + "</timg:VideoSourceToken>"
			+ "<timg:ImagingSettings>"
			+ "<tt:Exposure>"
			+ "<tt:Mode>MANUAL</tt:Mode>"
			+ "<tt:Iris>" + irisValue + "</tt:Iris>"
			+ "</tt:Exposure>"
			+ "</timg:ImagingSettings>"
			+ "</timg:SetImagingSettings>";
		sendSoap(url, NS_IMG + "/SetImagingSettings", body, username, password, host, port);
	}

	// ============================== 辅助命令 ==============================

	/** 发送辅助命令，例如 {@code tt:Wiper|On}、{@code tt:IRLamp|Off}。 */
	public static void sendAuxiliaryCommand(String host, int port,
			String username, String password,
			String profileToken, String auxCommand) {
		String url = baseUrl(host, port) + "/onvif/PTZ";
		String body = "<tptz:SendAuxiliaryCommand xmlns:tptz=\"" + NS_PTZ + "\">"
			+ "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>"
			+ "<tptz:AuxiliaryData>" + auxCommand + "</tptz:AuxiliaryData>"
			+ "</tptz:SendAuxiliaryCommand>";
		sendSoap(url, NS_PTZ + "/SendAuxiliaryCommand", body, username, password, host, port);
	}

	public static void wiperOn(String host, int port,
			String username, String password, String profileToken) {
		sendAuxiliaryCommand(host, port, username, password, profileToken, "tt:Wiper|On");
	}
	public static void wiperOff(String host, int port,
			String username, String password, String profileToken) {
		sendAuxiliaryCommand(host, port, username, password, profileToken, "tt:Wiper|Off");
	}
	public static void irOn(String host, int port,
			String username, String password, String profileToken) {
		sendAuxiliaryCommand(host, port, username, password, profileToken, "tt:IRLamp|On");
	}
	public static void irOff(String host, int port,
			String username, String password, String profileToken) {
		sendAuxiliaryCommand(host, port, username, password, profileToken, "tt:IRLamp|Off");
	}

	// ============================== SOAP 基础设施 ==============================

	private static String sendSoap(String url, String action, String bodyXml,
			String username, String password, String host, int port) {
		String envelope = buildEnvelope(bodyXml, username, password);
		log.debug("[ONVIF] POST {} action={}\n{}", url, action, envelope);
		try (HttpResponse resp = HttpRequest.post(url)
			.header("Content-Type", "application/soap+xml; charset=utf-8; action=\"" + action + "\"")
			.body(envelope, "application/soap+xml; charset=utf-8")
			.timeout(DEFAULT_TIMEOUT_MS)
			.execute()) {
			String body = resp.body();
			log.debug("[ONVIF] <= status={} body=\n{}", resp.getStatus(), StrUtil.maxLength(body, 4000));
			if (resp.getStatus() >= 400 || (body != null && body.contains(":Fault"))) {
				log.warn("[ONVIF] 调用失败 status={} resp={}", resp.getStatus(), StrUtil.maxLength(body, 800));
				throw new OnvifException("ONVIF 调用失败：" + extractFault(body));
			}
			return body == null ? "" : body;
		} catch (OnvifException e) {
			throw e;
		} catch (Exception e) {
			throw new OnvifException("ONVIF 请求异常 " + host + ":" + port + " - " + e.getMessage(), e);
		}
	}

	private static String buildEnvelope(String bodyXml, String username, String password) {
		String securityHeader = StrUtil.isBlank(username) ? "" : buildSecurityHeader(username, password);
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<s:Envelope xmlns:s=\"" + NS_S + "\">"
			+ "<s:Header>" + securityHeader + "</s:Header>"
			+ "<s:Body>" + bodyXml + "</s:Body>"
			+ "</s:Envelope>";
	}

	private static String buildSecurityHeader(String username, String password) {
		byte[] nonceBytes = IdUtil.simpleUUID().substring(0, 16).getBytes(StandardCharsets.UTF_8);
		String nonceB64 = Base64.encode(nonceBytes);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String created = sdf.format(new Date());
		String pwd = password == null ? "" : password;
		String digest = sha1Base64(concat(nonceBytes, created.getBytes(StandardCharsets.UTF_8),
			pwd.getBytes(StandardCharsets.UTF_8)));
		return "<wsse:Security s:mustUnderstand=\"1\" "
			+ "xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" "
			+ "xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">"
			+ "<wsse:UsernameToken>"
			+ "<wsse:Username>" + username + "</wsse:Username>"
			+ "<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">"
			+ digest + "</wsse:Password>"
			+ "<wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">"
			+ nonceB64 + "</wsse:Nonce>"
			+ "<wsu:Created>" + created + "</wsu:Created>"
			+ "</wsse:UsernameToken>"
			+ "</wsse:Security>";
	}

	private static String sha1Base64(byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			return Base64.encode(md.digest(data));
		} catch (Exception e) {
			throw new OnvifException("SHA-1 计算失败: " + e.getMessage(), e);
		}
	}

	private static byte[] concat(byte[]... arrays) {
		int len = 0;
		for (byte[] a : arrays) len += a.length;
		byte[] out = new byte[len];
		int pos = 0;
		for (byte[] a : arrays) {
			System.arraycopy(a, 0, out, pos, a.length);
			pos += a.length;
		}
		return out;
	}

	private static String extractFault(String body) {
		if (StrUtil.isBlank(body)) return "无响应";
		String reason = ReUtil.getGroup1("<[^>]*Text[^>]*>([^<]+)</[^>]*Text>", body);
		if (StrUtil.isNotBlank(reason)) return translateFault(reason);
		String fs = ReUtil.getGroup1("<faultstring>([^<]+)</faultstring>", body);
		return StrUtil.isNotBlank(fs) ? translateFault(fs) : StrUtil.maxLength(body, 200);
	}

	private static String translateFault(String raw) {
		if (raw == null) return "未知错误";
		String s = raw.trim();
		if (s.contains("AuxiliaryCommand is not supported") || s.contains("Aux command not supported")) {
			return "设备不支持该辅助命令（如雨刷/红外灯）";
		}
		if (s.contains("settings are incorrect") || s.contains("Settings invalid")) {
			return "参数不符合设备要求（取值范围或字段格式错误）";
		}
		if (s.contains("Not Authorized") || s.contains("NotAuthorized")) {
			return "鉴权失败，请检查用户名/密码与设备时间";
		}
		if (s.contains("ActionNotSupported")) {
			return "设备不支持该操作";
		}
		return s;
	}

	// ============================== DeviceInformation ==============================

	/** 设备信息 DTO：GetDeviceInformation 的解析结果。 */
	public static class DeviceInformation {
		private final String manufacturer;
		private final String dbModel;
		private final String firmwareVersion;
		private final String serialNumber;
		private final String hardwareId;

		private DeviceInformation(String manufacturer, String dbModel, String firmwareVersion,
								  String serialNumber, String hardwareId) {
			this.manufacturer = manufacturer;
			this.dbModel = dbModel;
			this.firmwareVersion = firmwareVersion;
			this.serialNumber = serialNumber;
			this.hardwareId = hardwareId;
		}

		public String getManufacturer()     { return manufacturer; }
		public String getDbModel()             { return dbModel; }
		public String getFirmwareVersion()   { return firmwareVersion; }
		public String getSerialNumber()      { return serialNumber; }
		public String getHardwareId()         { return hardwareId; }

		@Override
		public String toString() {
			return "DeviceInfo{" +
				"厂商='" + manufacturer + '\'' +
				", 型号='" + dbModel + '\'' +
				", 固件='" + firmwareVersion + '\'' +
				", 序列号='" + serialNumber + '\'' +
				", 硬件ID='" + hardwareId + '\'' +
				'}';
		}
	}

	// ============================== ProfileInfo ==============================

	/** 通道信息：token 与名称。 */
	public static class ProfileInfo {
		private final String token;
		private final String name;

		private ProfileInfo(String token, String name) {
			this.token = token;
			this.name = name;
		}

		public String getToken() { return token; }
		public String getName()  { return name; }

		@Override
		public String toString() {
			return "ProfileInfo{token='" + token + "', name='" + name + "'}";
		}
	}

	// ============================== PresetInfo ==============================

	/** 预置位信息：token 与名称。 */
	public static class PresetInfo {
		private final String token;
		private final String name;

		private PresetInfo(String token, String name) {
			this.token = token;
			this.name = name;
		}

		public String getToken() { return token; }
		public String getName()  { return name; }

		@Override
		public String toString() {
			return "PresetInfo{token='" + token + "', name='" + name + "'}";
		}
	}

	// ============================== DiscoveryInfo ==============================

	/** WS-Discovery 发现的设备信息。 */
	public static class DiscoveryInfo {
		private final String ip;
		private final String hostName;

		private DiscoveryInfo(String ip, String hostName) {
			this.ip = ip;
			this.hostName = hostName;
		}

		public String getIp()       { return ip; }
		public String getHostName() { return hostName; }

		@Override
		public String toString() {
			return "DiscoveryInfo{ip='" + ip + "', hostName='" + hostName + "'}";
		}
	}
}
