package com.cmit.testing.entity.app;

import java.util.Date;

public class AppScript {
    /**
     * 脚本ID
     */
    private Integer scriptId;

    /**
     * 脚本名称
     */
    private String scriptName;

    /**
     * 脚本标识：脚本标识：0-通用，1-普通
     */
    private Integer scriptTag;

    /**
     * 业务ID
     */
    private Integer businessId;

    /**
     * 脚本类型
     */
    private Integer scriptType;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Integer createPerson;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Integer updatePerson;

    /**
     * 排序
     */
    private Integer orderCode;

    /**
     * 屏幕高度
     */
    private String phoneHeight;

    /**
     * 屏幕宽度
     */
    private String phoneWidth;

    /**
     * 是否清除数据：0-否；1-是
     */
    private Integer isClear;
    /**
     * 是否安装apk：0-安装；1-卸载
     */
    private Integer isInstall;
    /**
     * 是否卸载apk：0-否；1-是
     */
    private Integer isUninstall;
    /**
     * 脚本zip文件名称
     */
    private String scriptFileName;

    /**
     * 脚本zip包存放路径
     */
    private String scriptPath;

    /**
     * MD5值
     */
    private String md5Code;

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public Integer getScriptTag() {
        return scriptTag;
    }

    public void setScriptTag(Integer scriptTag) {
        this.scriptTag = scriptTag;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getScriptType() {
        return scriptType;
    }

    public void setScriptType(Integer scriptType) {
        this.scriptType = scriptType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(Integer createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(Integer updatePerson) {
        this.updatePerson = updatePerson;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
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

    public String getScriptFileName() {
        return scriptFileName;
    }

    public void setScriptFileName(String scriptFileName) {
        this.scriptFileName = scriptFileName;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public String getMd5Code() {
        return md5Code;
    }

    public void setMd5Code(String md5Code) {
        this.md5Code = md5Code;
    }

    public Integer getIsInstall() {
        return isInstall;
    }

    public void setIsInstall(Integer isInstall) {
        this.isInstall = isInstall;
    }
}