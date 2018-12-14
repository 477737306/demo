package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description TODO Proxy执行脚本具体情况
 * @date 2018/10/13 0013.
 */
public class CurrExecuteStep {

    /**
     * 用例ID
     */
    private Integer appCaseId;
    /**
     * 设备唯一标识码
     */
    private String deviceSn;
    /**
     * 当前步骤
     */
    private Integer currentStep;
    /**
     * 总步骤
     */
    private Integer totalStep;

    private String currentScript;

    private Integer currentCircleTimes;

    public Integer getAppCaseId() {
        return appCaseId;
    }

    public void setAppCaseId(Integer appCaseId) {
        this.appCaseId = appCaseId;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public Integer getTotalStep() {
        return totalStep;
    }

    public void setTotalStep(Integer totalStep) {
        this.totalStep = totalStep;
    }

    public String getCurrentScript() {
        return currentScript;
    }

    public void setCurrentScript(String currentScript) {
        this.currentScript = currentScript;
    }

    public Integer getCurrentCircleTimes() {
        return currentCircleTimes;
    }

    public void setCurrentCircleTimes(Integer currentCircleTimes) {
        this.currentCircleTimes = currentCircleTimes;
    }
}
