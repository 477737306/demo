package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/5 0005 上午 11:30.
 */
public class BaseApk {

    /**
     * 卸载的应用的包名
     */
    private String packageName;
    /**
     * 卸载的应用版本号
     */
    private String appVersion;

    /********************下面字段用于结果返回********************/
    /**
     * 0:成功  1:失败  2: 手机无此apk
     */
    private String result;

    /**
     * 消息说明
     */
    private String msg;

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
