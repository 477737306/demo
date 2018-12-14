package com.cmit.testing.entity.vo;

import java.util.Date;

/**
 * 执行过程表
 */
public class CaseExcProcessVO {

    //app_script表字段
    /**
     * 脚本名称
     */
    private String scriptName;
    /**
     * 脚本类型
     */
    private Integer scriptType;


    //app_case_device表字段
    /**
     * 主键
     */
    private Integer id;
    /**
     * 省份
     */
    private String province;
    /**
     * 手机号码
     */
    private String telNum;
    /**
     * 执行状态
     */
    private Integer executeStatus;
    /**
     * 执行结果
     */
    private Integer passStatus;
    /**
     * 时间
     */
    private Date executeTime;

    //app_device表字段
    /**
     * 执行手机
     */
    private String deviceBrand;

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public Integer getScriptType() {
        return scriptType;
    }

    public void setScriptType(Integer scriptType) {
        this.scriptType = scriptType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public Integer getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
    }

    public Integer getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(Integer passStatus) {
        this.passStatus = passStatus;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getBelonger() {
        return belonger;
    }

    public void setBelonger(String belonger) {
        this.belonger = belonger;
    }

    /**
     * 执行人（归属人）
     */
    private String belonger;
}
