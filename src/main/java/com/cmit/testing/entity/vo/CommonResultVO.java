package com.cmit.testing.entity.vo;

import com.cmit.testing.entity.app.AppRecordStep;

import java.util.Date;
import java.util.List;

public class CommonResultVO {

    /**
     * 执行结果ID
     */
    private Integer caseResultId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 业务名称
     */
    private String businessName;

    private String scriptName;
    /**
     * 手机号码
     */
    private String telNum;
    /**
     * 执行主机
     */
    private String deviceHost;

    private String deviceId;
    /**
     * 执行时间
     */
    private Date executeTime;
    /**
     * 副本用例ID
     */
    private String caseId;
    /**
     * 原用例ID
     */
    private String oldCaseId;
    /**
     * 用例编号
     */
    private String serialNumber;
    /**
     * 用例名称
     */
    private String caseName;
    /**
     * 省份
     */
    private String province;
    /**
     * 轮次
     */
    private Integer executeNum;
    /**
     * 执行结果
     */
    private String executeResult;
    /**
     * 通过状态
     */
    private String passStatus;
    /**
     * 录屏地址
     */
    private String recordVideoUrl;
    /**
     * 截图地址
     */
    private String screenshotUrl;
    /**
     * 日志文件地址
     */
    private String logUrl;
    /**
     * 缺陷描述
     */
    private String defectDesc;
    /**
     * 类型：app, web
     */
    private String type;
    /**
     * 完成状态
     */
    private String isFinish;
    /**
     * 执行人
     */
    private String executePerson;

    /**
     * 步骤记录详情列表
     */
    private List<AppRecordStep> recordStepList;

    /**
     * 用户勾选的所有用例结果ID
     */
    private List<Integer> ids;

    public Integer getCaseResultId() {
        return caseResultId;
    }

    public void setCaseResultId(Integer caseResultId) {
        this.caseResultId = caseResultId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getDeviceHost() {
        return deviceHost;
    }

    public void setDeviceHost(String deviceHost) {
        this.deviceHost = deviceHost;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getOldCaseId() {
        return oldCaseId;
    }

    public void setOldCaseId(String oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getExecuteNum() {
        return executeNum;
    }

    public void setExecuteNum(Integer executeNum) {
        this.executeNum = executeNum;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(String passStatus) {
        this.passStatus = passStatus;
    }

    public String getRecordVideoUrl() {
        return recordVideoUrl;
    }

    public void setRecordVideoUrl(String recordVideoUrl) {
        this.recordVideoUrl = recordVideoUrl;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getDefectDesc() {
        return defectDesc;
    }

    public void setDefectDesc(String defectDesc) {
        this.defectDesc = defectDesc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public List<AppRecordStep> getRecordStepList() {
        return recordStepList;
    }

    public void setRecordStepList(List<AppRecordStep> recordStepList) {
        this.recordStepList = recordStepList;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
