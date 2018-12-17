package com.cmit.testing.entity.app;

import java.io.Serializable;
import java.util.Date;

public class AppScriptStep implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 脚本步骤ID
     */
    private Integer scriptStepId;

    /**
     * 脚本XML文件ID
     */
    private Integer scriptFileId;

    /**
     * 脚本ID
     */
    private Integer scriptId;

    /**
     * 脚本步骤：第几步执行
     */
    private String stepNo;
    /**
     * 步骤描述
     */
    private String stepDesc;

    /**
     * 步骤参数类型(-1:普通文本输入、0:输入账户、1:输入密码；8-动态验证码)
     */
    private String stepCode;

    /**
     * 动作类型
     */
    private String actionType;

    /**
     * 存放一些自定义输入的值
     */
    private String inputValue;

    /**
     * xpath值
     */
    private String xpath;

    /**
     * 定位：坐标
     */
    private String coordinate;

    /**
     * 动作文本
     */
    private String actionText;

    /**
     * 图片名称，用于图像识别的图片
     */
    private String picName;

    private Date createTime;

    private Integer createPerson;

    private Date updateTime;

    private Integer updatePerson;

    public Integer getScriptStepId() {
        return scriptStepId;
    }

    public void setScriptStepId(Integer scriptStepId) {
        this.scriptStepId = scriptStepId;
    }

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

    public String getStepNo() {
        return stepNo;
    }

    public void setStepNo(String stepNo) {
        this.stepNo = stepNo == null ? null : stepNo.trim();
    }

    public String getStepCode() {
        return stepCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode == null ? null : stepCode.trim();
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType == null ? null : actionType.trim();
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath == null ? null : xpath.trim();
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate == null ? null : coordinate.trim();
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText == null ? null : actionText.trim();
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName == null ? null : picName.trim();
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

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    @Override
    public String toString() {
        return "AppScriptStep{" +
                "scriptStepId=" + scriptStepId +
                ", scriptFileId=" + scriptFileId +
                ", scriptId=" + scriptId +
                ", stepNo='" + stepNo + '\'' +
                ", stepCode='" + stepCode + '\'' +
                ", actionType='" + actionType + '\'' +
                ", inputValue='" + inputValue + '\'' +
                ", xpath='" + xpath + '\'' +
                ", coordinate='" + coordinate + '\'' +
                ", actionText='" + actionText + '\'' +
                ", picName='" + picName + '\'' +
                ", createTime=" + createTime +
                ", createPerson=" + createPerson +
                ", updateTime=" + updateTime +
                ", updatePerson=" + updatePerson +
                '}';
    }
}