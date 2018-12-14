package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/10/12 0012.
 */
public class CurrentExecuteCase {

    /**
     * 设备唯一标识码
     */
    private String deviceSn;

    /**
     * 用例ID
     */
    private Integer appCaseId;

    /**
     * 0:表示计时 1：表示计步
     */
    private Integer countType;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public Integer getAppCaseId() {
        return appCaseId;
    }

    public void setAppCaseId(Integer appCaseId) {
        this.appCaseId = appCaseId;
    }

    public Integer getCountType() {
        return countType;
    }

    public void setCountType(Integer countType) {
        this.countType = countType;
    }
}
