package com.cmit.testing.entity.proxy;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO 批量卸载请求对象
 * @date 2018/9/5 0005 上午 11:32.
 */
public class BatchUnInstallApkRequest {
    /**
     * 待操作手机的唯一序列号
     */
    private String deviceSn;

    /**
     * 批量卸载的app信息
     */
    private List<BaseApk> appInfos;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public List<BaseApk> getAppInfos() {
        return appInfos;
    }

    public void setAppInfos(List<BaseApk> appInfos) {
        this.appInfos = appInfos;
    }
}
