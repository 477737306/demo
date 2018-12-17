package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description  真机操作  安装应用/卸载应用
 * @date 2018/9/5 0005 上午 10:56.
 */
public class AppOperate {

    /**
     * 终端手机唯一序列号
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
     * apk下载路径
     */
    private String uri;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
