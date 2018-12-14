package com.cmit.testing.entity.proxy;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO 用例执行完成后，Proxy返回的执行结果信息
 * @date 2018/9/17 0017.
 */
public class TestResultInfo {

    private String deviceSn;

    private Integer appCaseId;

    private String result;

    private String msg;

    /**
     * 日志文件路径
     */
    private String logUrl;
    /**
     * 截图文件路径集合(2018/10/24 废弃，并将该字段放在StepDetails中)
     */
    private List<String> screenShotUrl;
    /**
     * 录屏文件路径
     */
    private String videoUrl;

    /**
     * 步骤详情
     */
    private List<StepDetails> stepDetails;

    private String testResultZip;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTestResultZip() {
        return testResultZip;
    }

    public void setTestResultZip(String testResultZip) {
        this.testResultZip = testResultZip;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public List<String> getScreenShotUrl() {
        return screenShotUrl;
    }

    public void setScreenShotUrl(List<String> screenShotUrl) {
        this.screenShotUrl = screenShotUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<StepDetails> getStepDetails() {
        return stepDetails;
    }

    public void setStepDetails(List<StepDetails> stepDetails) {
        this.stepDetails = stepDetails;
    }

    @Override
    public String toString() {
        return "TestResultInfo{" +
                "deviceSn='" + deviceSn + '\'' +
                ", appCaseId=" + appCaseId +
                ", result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                ", logUrl='" + logUrl + '\'' +
                ", screenShotUrl=" + screenShotUrl +
                ", videoUrl='" + videoUrl + '\'' +
                ", stepDetails=" + stepDetails +
                ", testResultZip='" + testResultZip + '\'' +
                '}';
    }
}
