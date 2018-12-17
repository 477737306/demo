package com.cmit.testing.entity.app;

import java.util.Date;

public class AppCaseResult {

    private Integer resultId;
    /**
     * app_case_device表中的ID
     */
    private Integer executeId;
    /**
     * 副本用例ID
     */
    private Integer caseId;
    /**
     * 原用例ID
     */
    private Integer oldCaseId;

    private Integer deviceId;

    private String logUrl;

    private Integer isFinish;

    private Integer executeResult;

    private Integer passStatus;

    private String screenshotUrl;

    private String recordVideoUrl;

    private String resultUrl;

    private String defectDesc;

    private Integer clickNum;

    private Integer inputNum;

    private double consumeTime;

    /**
     * 执行人
     */
    private Integer executePerson;
    /**
     * 报告生成
     */
    private Date createTime;

    public double getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(double consumeTime) {
        this.consumeTime = consumeTime;
    }

    public Integer getInputNum() {
        return inputNum;
    }

    public void setInputNum(Integer inputNum) {
        this.inputNum = inputNum;
    }

    public Integer getClickNum() {
        return clickNum;
    }

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public Integer getExecuteId() {
        return executeId;
    }

    public void setExecuteId(Integer executeId) {
        this.executeId = executeId;
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

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl == null ? null : logUrl.trim();
    }

    public Integer getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Integer isFinish) {
        this.isFinish = isFinish;
    }

    public Integer getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(Integer executeResult) {
        this.executeResult = executeResult;
    }

    public Integer getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(Integer passStatus) {
        this.passStatus = passStatus;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl == null ? null : screenshotUrl.trim();
    }

    public String getRecordVideoUrl() {
        return recordVideoUrl;
    }

    public void setRecordVideoUrl(String recordVideoUrl) {
        this.recordVideoUrl = recordVideoUrl == null ? null : recordVideoUrl.trim();
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl == null ? null : resultUrl.trim();
    }

    public String getDefectDesc() {
        return defectDesc;
    }

    public void setDefectDesc(String defectDesc) {
        this.defectDesc = defectDesc == null ? null : defectDesc.trim();
    }

    public Integer getOldCaseId() {
        return oldCaseId;
    }

    public void setOldCaseId(Integer oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public Integer getExecutePerson() {
        return executePerson;
    }

    public void setExecutePerson(Integer executePerson) {
        this.executePerson = executePerson;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}