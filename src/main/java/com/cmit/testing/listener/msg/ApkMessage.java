package com.cmit.testing.listener.msg;

/**
 * 消息对象：后台推送给前端的消息
 */
public class ApkMessage {

    private String appName;

    private String packageName;

    private String appVersion;

    private String deviceSn;
    /**
     * 0-安装；1-卸载
     */
    private String type;
    /**
     * 结果：0-成功；1-失败；2-无此apk应用
     */
    private String result;

    private String msg;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "ApkMessage{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", deviceSn='" + deviceSn + '\'' +
                ", type='" + type + '\'' +
                ", result='" + result + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
