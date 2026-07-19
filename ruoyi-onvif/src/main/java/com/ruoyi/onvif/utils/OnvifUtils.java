package com.ruoyi.onvif.utils;

/**
 * ONVIF 工具类（开源版：核心协议实现已移除）
 * <p>
 * 仅保留 DTO 内部类供接口签名兼容使用。
 * 所有 ONVIF 协议交互功能（WS-Discovery、Device、Media、PTZ、Imaging 等）均已移除。
 */
public final class OnvifUtils {

	private OnvifUtils() {}

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
	}
}
