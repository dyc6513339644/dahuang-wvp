package com.ruoyi.alarms.constant;

/**
 * 统一报警模块常量。
 * <p>
 * 协议/来源/状态/级别这些<b>编码</b>参与程序判断，留在代码里；
 * 对应的<b>展示名称</b>只在字典中维护（见 {@link AlarmDictType}），代码不再保留名称副本。
 */
public class AlarmConstants {

    private AlarmConstants() {
    }

    /** 国标设备接入协议 */
    public static final String PROTOCOL_GB28181 = "GB28181";
    /** 海康 ISUP 协议 */
    public static final String PROTOCOL_ISUP = "ISUP";
    /** ONVIF 协议 */
    public static final String PROTOCOL_ONVIF = "ONVIF";
    /** 推流设备 */
    public static final String PROTOCOL_PUSH = "PUSH";
    /** 拉流设备 */
    public static final String PROTOCOL_PULL = "PULL";
    /** 其它/未知 */
    public static final String PROTOCOL_OTHER = "OTHER";

    /** 处理状态：未处理 */
    public static final int HANDLE_UNHANDLED = 0;
    /** 处理状态：已处理 */
    public static final int HANDLE_HANDLED = 1;
    /** 处理状态：已忽略 */
    public static final int HANDLE_IGNORED = 2;

    /** 报警级别：紧急 */
    public static final int LEVEL_CRITICAL = 1;
    /** 报警级别：重要 */
    public static final int LEVEL_MAJOR = 2;
    /** 报警级别：次要 */
    public static final int LEVEL_MINOR = 3;
    /** 报警级别：提示 */
    public static final int LEVEL_INFO = 4;

    /** ISUP 设备在平台内的 device_id 前缀（与 DeviceIsupServiceImpl 保持一致） */
    public static final String ISUP_DEVICE_PREFIX = "haikang_isup_";

    /** 原始报文最大保存长度，超出截断，避免大报文撑爆行 */
    public static final int RAW_PAYLOAD_MAX_LENGTH = 4000;

    /** 是否为已知协议 */
    public static boolean isKnownProtocol(String protocolType) {
        return PROTOCOL_GB28181.equals(protocolType)
                || PROTOCOL_ISUP.equals(protocolType)
                || PROTOCOL_ONVIF.equals(protocolType)
                || PROTOCOL_PUSH.equals(protocolType)
                || PROTOCOL_PULL.equals(protocolType)
                || PROTOCOL_OTHER.equals(protocolType);
    }
}
