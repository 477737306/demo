package com.cmit.testing.entity.app;

import java.util.Date;
import java.util.List;

public class AppCaseDevice {

    private Integer id;

    /**
     * 用例ID
     */
    private Integer caseId;

    /**
     * 终端ID
     */
    private Integer deviceId;
    /**
     * 类型：1-用于执行用例；2-用于展示到界面
     */
    private String paramType;

    /**
     * 用例轮询次数
     */
    private Integer executeNum;

    /**
     * 通过状态：0-未通过；1-通过；2-未执行
     */
    private Integer passStatus;
    /**
     * 失败原因
     */
    private String failureReason;
    /**
     * 执行状态：0-未执行，1-执行中，2-执行完成
     */
    private Integer executeStatus;

    /**
     * 用例是否下发消息：0-未下发；1-已下发；2-已收到消息
     */
    private Integer isPush;

    /**
     * 已下发
     */
    private Integer pushed;

  /**
     * 总步骤
     */
    private Integer totalSteps;
    /**
     * 当前步骤
     */
    private Integer currentStep;

    /**
     * 省份
     */
    private String province;

    /**
     * 手机号码
     */
    private String telNum;

    /**
     * 实际执行时间
     */
    private Date executeTime;

    /**
     * 终端唯一序列号
     */
    private String deviceSn;
    /**
     * 使用状态  0-空闲；1-占用；2-预占用
     */
    private String useStatus;
    /**
     * 在线状态  0-离线；1-在线
     */
    private String onlineStatus;
    /**
     * Proxy的ID
     */
    private Integer proxyId;
    /**
     * 设备ID集合
     */
    private List<Integer> deviceIds;

    private String caseName;
    private String oldCaseId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getExecuteNum() {
        return executeNum;
    }

    public void setExecuteNum(Integer executeNum) {
        this.executeNum = executeNum;
    }

    public Integer getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(Integer passStatus) {
        this.passStatus = passStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Integer getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum == null ? null : telNum.trim();
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getProxyId() {
        return proxyId;
    }

    public void setProxyId(Integer proxyId) {
        this.proxyId = proxyId;
    }

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public Integer getPushed() {
        return pushed;
    }

    public void setPushed(Integer pushed) {
        this.pushed = pushed;
    }

    public List<Integer> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<Integer> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getOldCaseId() {
        return oldCaseId;
    }

    public void setOldCaseId(String oldCaseId) {
        this.oldCaseId = oldCaseId;
    }
}