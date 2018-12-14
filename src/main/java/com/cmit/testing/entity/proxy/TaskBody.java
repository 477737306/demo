package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description TODO 任务zip包task.txt文件中的数据对象
 * @date 2018/9/12 0012.
 */
public class TaskBody {

    private Integer appCaseId;
    /**
     * 执行次数
     */
    private Integer retryCount;
    /**
     * 是否录屏
     */
    private Integer isRecord;
    /**
     * 是否卸载
     */
    private Integer isUninstall;
    /**
     * 是否卸载
     */
    private Integer isInstall;
    /**
     * 执行的脚本路径
     */
    private String jarPath;

    private String apkPath;

    private Integer execOrder;

    public Integer getAppCaseId() {
        return appCaseId;
    }

    public void setAppCaseId(Integer appCaseId) {
        this.appCaseId = appCaseId;
    }

    public Integer getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(Integer isRecord) {
        this.isRecord = isRecord;
    }

    public Integer getIsUninstall() {
        return isUninstall;
    }

    public void setIsUninstall(Integer isUninstall) {
        this.isUninstall = isUninstall;
    }

    public Integer getIsInstall() {
        return isInstall;
    }

    public void setIsInstall(Integer isInstall) {
        this.isInstall = isInstall;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public Integer getExecOrder() {
        return execOrder;
    }

    public void setExecOrder(Integer execOrder) {
        this.execOrder = execOrder;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
}
