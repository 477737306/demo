package com.cmit.testing.entity.vo;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO 用于展示给页面的脚本数据对象
 * @date 2018/9/18 0018.
 */
public class ScriptVO {

    private Integer scriptId;

    private Integer businessId;

    private Integer scriptTag;

    private Integer scriptType;

    private String scriptName;

    private String description;

    private Integer isClear;

    private Integer isUninstall;

    private Integer isInstall;

    private String phoneHeight;

    private String phoneWidth;
    /**
     * 脚本zip文件在FastDFS上的存储路径
     */
    private String scriptPath;
    /**
     * 上传脚本时存放的临时路径
     */
    private String scriptTempPath;

    private String appName;

    private String appVersion;

    private String packageName;

    private Integer appId;

    private Integer scriptFileId;

    private List<AppStepVO> stepList;

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getScriptTag() {
        return scriptTag;
    }

    public void setScriptTag(Integer scriptTag) {
        this.scriptTag = scriptTag;
    }

    public Integer getScriptType() {
        return scriptType;
    }

    public void setScriptType(Integer scriptType) {
        this.scriptType = scriptType;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public List<AppStepVO> getStepList() {
        return stepList;
    }

    public void setStepList(List<AppStepVO> stepList) {
        this.stepList = stepList;
    }

    public Integer getIsClear() {
        return isClear;
    }

    public void setIsClear(Integer isClear) {
        this.isClear = isClear;
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

    public String getPhoneHeight() {
        return phoneHeight;
    }

    public void setPhoneHeight(String phoneHeight) {
        this.phoneHeight = phoneHeight;
    }

    public String getPhoneWidth() {
        return phoneWidth;
    }

    public void setPhoneWidth(String phoneWidth) {
        this.phoneWidth = phoneWidth;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public Integer getScriptFileId() {
        return scriptFileId;
    }

    public void setScriptFileId(Integer scriptFileId) {
        this.scriptFileId = scriptFileId;
    }

    public String getScriptTempPath() {
        return scriptTempPath;
    }

    public void setScriptTempPath(String scriptTempPath) {
        this.scriptTempPath = scriptTempPath;
    }
}
