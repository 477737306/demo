package com.cmit.testing.entity.app;

import java.util.List;

public class ExecuteCaseBO {

    /**
     * 执行用例的原用例ID
     */
    private Integer caseId;
    /**
     * 业务ID
     */
    private Integer businessId;
    /**
     * 执行类型：众测任务执行、批量任务执行、单独执行
     */
    private String type;
    /**
     * 众测任务ID
     */
    private Integer surveyTaskId;
    /**
     * 批量任务ID
     */
    private Integer sysTaskId;
    /**
     * 批量执行的用例ID集合
     */
    private List<Integer> caseIds;
    /**
     * 执行用例的设备集合
     */
    private List<AppCaseDevice> deviceList;

    /**
     * 执行人名称和ID
     */
    private String executorName;
    private Integer executorId;
    /**
     * 实际第几次执行
     */
    private Integer realExecuteNum;

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSurveyTaskId() {
        return surveyTaskId;
    }

    public void setSurveyTaskId(Integer surveyTaskId) {
        this.surveyTaskId = surveyTaskId;
    }

    public Integer getSysTaskId() {
        return sysTaskId;
    }

    public void setSysTaskId(Integer sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public List<Integer> getCaseIds() {
        return caseIds;
    }

    public void setCaseIds(List<Integer> caseIds) {
        this.caseIds = caseIds;
    }

    public List<AppCaseDevice> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<AppCaseDevice> deviceList) {
        this.deviceList = deviceList;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public Integer getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Integer executorId) {
        this.executorId = executorId;
    }

    public Integer getRealExecuteNum() {
        return realExecuteNum;
    }

    public void setRealExecuteNum(Integer realExecuteNum) {
        this.realExecuteNum = realExecuteNum;
    }
}
