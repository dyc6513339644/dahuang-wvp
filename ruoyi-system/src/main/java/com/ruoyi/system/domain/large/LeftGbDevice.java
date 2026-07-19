package com.ruoyi.system.domain.large;

/**
 * @FileName LeftGbDevice
 * @Description
 * @Author fengcheng
 * @date 2025-05-01
 **/
public class LeftGbDevice {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备厂家
     */
    private String manufacturer;

    /**
     * 在线状态
     */
    private Integer onLine;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getOnLine() {
        return onLine;
    }

    public void setOnLine(Integer onLine) {
        this.onLine = onLine;
    }

    @Override
    public String toString() {
        return "LeftGbDevice{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", onLine=" + onLine +
                '}';
    }
}
