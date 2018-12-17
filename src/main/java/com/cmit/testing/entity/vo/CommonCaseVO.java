package com.cmit.testing.entity.vo;

import com.cmit.testing.entity.Parameter;

import java.util.Date;
import java.util.List;

public class CommonCaseVO {

    private Integer id;

    private Integer sysTaskId;

    private Integer bSysTaskId;//业务任务id

    private String type;

    private int phoneNumber;//电话号码个数

    private Integer executeNumber; //执行的总次数

    private List<Integer> unequalFinshs;    //isFinish不等于什么

    private Integer surveyTaskId; //众测任务id

    private String serialNumber;

    private String name;

    private Integer businessId;

    private Integer scriptId;

    private Integer retry;

    private String followId;

    private Integer isFinish;

    private String information;

    private Integer isRecord;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private Integer successNum;

    private Integer failureNum;

    private Double successRate;

    private Integer code;

    private Integer executionType;

    private String executionDate;

    private Integer excuteNum;

    private Integer oldTestcaseId;

    private List<Parameter> params;

    private String flag;

    private Integer isMerge;

    private String businessName;

    private Integer mergeNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSysTaskId() {
        return sysTaskId;
    }

    public void setSysTaskId(Integer sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public Integer getbSysTaskId() {
        return bSysTaskId;
    }

    public void setbSysTaskId(Integer bSysTaskId) {
        this.bSysTaskId = bSysTaskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getExecuteNumber() {
        return executeNumber;
    }

    public void setExecuteNumber(Integer executeNumber) {
        this.executeNumber = executeNumber;
    }

    public List<Integer> getUnequalFinshs() {
        return unequalFinshs;
    }

    public void setUnequalFinshs(List<Integer> unequalFinshs) {
        this.unequalFinshs = unequalFinshs;
    }

    public Integer getSurveyTaskId() {
        return surveyTaskId;
    }

    public void setSurveyTaskId(Integer surveyTaskId) {
        this.surveyTaskId = surveyTaskId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public Integer getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Integer isFinish) {
        this.isFinish = isFinish;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Integer getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(Integer isRecord) {
        this.isRecord = isRecord;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getExecutionType() {
        return executionType;
    }

    public void setExecutionType(Integer executionType) {
        this.executionType = executionType;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public Integer getExcuteNum() {
        return excuteNum;
    }

    public void setExcuteNum(Integer excuteNum) {
        this.excuteNum = excuteNum;
    }

    public Integer getOldTestcaseId() {
        return oldTestcaseId;
    }

    public void setOldTestcaseId(Integer oldTestcaseId) {
        this.oldTestcaseId = oldTestcaseId;
    }

    public List<Parameter> getParams() {
        return params;
    }

    public void setParams(List<Parameter> params) {
        this.params = params;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getIsMerge() {
        return isMerge;
    }

    public void setIsMerge(Integer isMerge) {
        this.isMerge = isMerge;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getMergeNum() {
        return mergeNum;
    }

    public void setMergeNum(Integer mergeNum) {
        this.mergeNum = mergeNum;
    }
}
