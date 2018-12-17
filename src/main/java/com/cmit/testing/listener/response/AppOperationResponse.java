package com.cmit.testing.listener.response;

/**
 * 应用安装/卸载 响应对象
 */
public class AppOperationResponse {

    /**
     * 设备序列号
     */
    private String deviceSn;
    /**
     * 结果：0-成功；1-失败；2-无此APK
     */
    private String result;
    /**
     * 应用版本号
     */
    private String appVersion;
    /**
     * 应用包名
     */
    private String packageName;
    /**
     * 消息
     */
    private String msg;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "AppOperationResponse{" +
                "deviceSn='" + deviceSn + '\'' +
                ", result='" + result + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", packageName='" + packageName + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
