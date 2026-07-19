package com.ruoyi.system.domain.large;

/**
 * @FileName CountGbNum
 * @Description
 * @Author fengcheng
 * @date 2025-05-01
 **/
public class CountGbNum {

    /**
     * 总数量
     */
    private Integer totalNum;

    /**
     * 离线数量
     */
    private Integer offlineNum;

    /**
     * 在线数量
     */
    private Integer onlineNum;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(Integer offlineNum) {
        this.offlineNum = offlineNum;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    @Override
    public String toString() {
        return "CountGbNum{" +
                "totalNum=" + totalNum +
                ", offlineNum=" + offlineNum +
                ", onlineNum=" + onlineNum +
                '}';
    }
}
