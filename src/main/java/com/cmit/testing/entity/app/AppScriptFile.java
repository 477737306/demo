package com.cmit.testing.entity.app;

public class AppScriptFile {
    /**
     * 脚本文件ID
     */
    private Integer scriptFileId;

    /**
     * 脚本ID
     */
    private Integer scriptId;

    /**
     * 执行顺序
     */
    private Integer orderCode;
    /**
     * 应用ID
     */
    private Integer appId;
    /**
     * 包名
     */
    private String packageName;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用版本
     */
    private String appVersion;

    /**
     * 执行jar文件的MD5值
     */
    private String md5Code;

    /**
     * 执行jar文件存放路径
     */
    private String executeJarUrl;

    /**
     * pics临时存放路径
     */
    private String picsTempPath;

    /**
     * 脚本XMl文件存放路径
     */
    private String scriptXmlPath;
    /**
     * 脚本的步骤总数
     */
    private Integer stepCount;

    public Integer getScriptFileId() {
        return scriptFileId;
    }

    public void setScriptFileId(Integer scriptFileId) {
        this.scriptFileId = scriptFileId;
    }

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    public String getMd5Code() {
        return md5Code;
    }

    public void setMd5Code(String md5Code) {
        this.md5Code = md5Code == null ? null : md5Code.trim();
    }

    public String getExecuteJarUrl() {
        return executeJarUrl;
    }

    public void setExecuteJarUrl(String executeJarUrl) {
        this.executeJarUrl = executeJarUrl == null ? null : executeJarUrl.trim();
    }

    public String getPicsTempPath() {
        return picsTempPath;
    }

    public void setPicsTempPath(String picsTempPath) {
        this.picsTempPath = picsTempPath;
    }

    public String getScriptXmlPath() {
        return scriptXmlPath;
    }

    public void setScriptXmlPath(String scriptXmlPath) {
        this.scriptXmlPath = scriptXmlPath == null ? null : scriptXmlPath.trim();
    }

    public Integer getStepCount() {
        return stepCount;
    }

    public void setStepCount(Integer stepCount) {
        this.stepCount = stepCount;
    }
}