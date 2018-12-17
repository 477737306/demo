package com.cmit.testing.entity.proxy;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/5 0005 上午 11:35.
 */
public class BatchUnInstallApkResponse {
    /**
     * 设备唯一序列号
     */
    private String deviceSn;

    /**
     * 批量卸载的app结果信息
     */
    private List<BaseApk> uninstallAppInfos;
    /**
     * 总的批量卸载结果，0:成功  1:失败
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

    public List<BaseApk> getUninstallAppInfos() {
        return uninstallAppInfos;
    }

    public void setUninstallAppInfos(List<BaseApk> uninstallAppInfos) {
        this.uninstallAppInfos = uninstallAppInfos;
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
