package com.cmit.testing.entity.vo;

import java.util.Date;

public class AppExecutingTaskVO {

    private Integer id;
    /**
     * 副本用例ID
     */
    private Integer caseId;
    /**
     * 原用例ID
     */
    private Integer oldCaseId;
    /**
     * 用例名称
     */
    private String caseName;

    private Integer deviceId;
    private String deviceSn;
    /**
     * 执行总步数
     */
    private Integer totalSteps;
    /**
     * 当前执行的步骤
     */
    private Integer currentStep;

    private Date executeTime;

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

    public Integer getOldCaseId() {
        return oldCaseId;
    }

    public void setOldCaseId(Integer oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
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

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
}
