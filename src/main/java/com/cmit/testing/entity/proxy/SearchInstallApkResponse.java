package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/5 0005 上午 11:40.
 */
public class SearchInstallApkResponse {
    /**
     * 设备唯一序列号
     */
    private String deviceSn;

    /**
     * 查询的应用的包名
     */
    private String packageName;

    /**
     * 查询的应用版本号
     */
    private String appVersion;

    /**
     * -1：查询失败（手机异常）；0：手机上无此应用 1：手机上有此应用
     */
    private String result;

    /**
     * 消息说明
     */
    private String msg;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
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
}
