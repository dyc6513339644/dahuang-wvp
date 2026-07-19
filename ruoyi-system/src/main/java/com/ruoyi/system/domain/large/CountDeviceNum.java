package com.ruoyi.system.domain.large;

/**
 * @FileName CountDeviceNum
 * @Description
 * @Author fengcheng
 * @date 2025-05-01
 **/
public class CountDeviceNum {

    /**
     * 国标设备数量
     */
    private Integer totalGbNum;

    /**
     * RTSP设备数量
     */
    private Integer totalRtspNum;

    /**
     * ONVIF设备数量
     */
    private Integer totalOnvifNum;

    public Integer getTotalGbNum() {
        return totalGbNum;
    }

    public void setTotalGbNum(Integer totalGbNum) {
        this.totalGbNum = totalGbNum;
    }

    public Integer getTotalRtspNum() {
        return totalRtspNum;
    }

    public void setTotalRtspNum(Integer totalRtspNum) {
        this.totalRtspNum = totalRtspNum;
    }

    public Integer getTotalOnvifNum() {
        return totalOnvifNum;
    }

    public void setTotalOnvifNum(Integer totalOnvifNum) {
        this.totalOnvifNum = totalOnvifNum;
    }

    @Override
    public String toString() {
        return "CountDeviceNum{" +
                "totalGbNum=" + totalGbNum +
                ", totalRtspNum=" + totalRtspNum +
                ", totalOnvifNum=" + totalOnvifNum +
                '}';
    }
}
