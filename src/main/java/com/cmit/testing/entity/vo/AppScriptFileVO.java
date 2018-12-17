package com.cmit.testing.entity.vo;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/12 0012.
 */
public class AppScriptFileVO {

    private Integer scriptFileId;

    private Integer orderCode;

    private String jarMd5Code;

    private String executeJarUrl;

    private Integer appId;

    private String appUrl;

    private String appMd5Code;

    public Integer getScriptFileId() {
        return scriptFileId;
    }

    public void setScriptFileId(Integer scriptFileId) {
        this.scriptFileId = scriptFileId;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public String getJarMd5Code() {
        return jarMd5Code;
    }

    public void setJarMd5Code(String jarMd5Code) {
        this.jarMd5Code = jarMd5Code;
    }

    public String getExecuteJarUrl() {
        return executeJarUrl;
    }

    public void setExecuteJarUrl(String executeJarUrl) {
        this.executeJarUrl = executeJarUrl;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppMd5Code() {
        return appMd5Code;
    }

    public void setAppMd5Code(String appMd5Code) {
        this.appMd5Code = appMd5Code;
    }
}
