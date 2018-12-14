package com.cmit.testing.entity.script;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO XML文件对应的脚本对象
 * @date 2018/9/6 0006 下午 3:28.
 */
@XmlRootElement(name = "Script")
@XmlType(propOrder = { "phoneWidth", "phoneHeight", "versionName","applicationName", "packageName","cleanData","uninstallApk","installApk","stepList"})
public class Script {

    private String phoneWidth;

    private String phoneHeight;

    /**
     * 版本
     */
    private String versionName;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 应用包名
     */
    private String packageName;

    /**
     * 是否清理数据
     */
    private String cleanData;

    /**
     * 是否卸载APK
     */
    private String uninstallApk;

    /**
     * 安装apk
     */
    private String installApk;

    private List<Step> stepList;

    public String getPhoneWidth() {
        return phoneWidth;
    }

    @XmlElement
    public void setPhoneWidth(String phoneWidth) {
        this.phoneWidth = phoneWidth;
    }

    public String getPhoneHeight() {
        return phoneHeight;
    }

    public void setPhoneHeight(String phoneHeight) {
        this.phoneHeight = phoneHeight;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCleanData() {
        return cleanData;
    }

    public void setCleanData(String cleanData) {
        this.cleanData = cleanData;
    }

    public String getUninstallApk() {
        return uninstallApk;
    }

    public void setUninstallApk(String uninstallApk) {
        this.uninstallApk = uninstallApk;
    }

    @XmlElement(name = "step")
    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    public String getInstallApk() {
        return installApk;
    }

    public void setInstallApk(String installApk) {
        this.installApk = installApk;
    }
}
