package com.cmit.testing.entity.app;

import com.cmit.testing.entity.vo.AppDeviceVO;

import java.util.Date;
import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/28 0028 下午 12:15.
 */
public class AppCase {

    private Integer caseId;
    private String caseNum;
    private String caseName;
    private String caseDesc;

    private String businessName;
    /**
     * 关联的业务ID
     */
    private Integer businessId;
    /**
     * 执行的脚本ID
     */
    private Integer scriptId;
    /**
     * 依赖ID
     * update by 2018-11-09
     * 依赖改为可以依赖多个用例
     * JSON字符串： {"app":[1,2,3], "web":[1,2,3]}
     */
    private String followId;
    /**
     * 复测次数
     */
    private Integer retryCount;
    /**
     * 真实的第几次执行，原用例无值，副本用例有值
     */
    private Integer realExecuteNum;

    /**
     * 用例zip包存放路径
     */
    private String taskZipPath;
    /**
     * Zip包的MD5值
     */
    private String zipMd5Code;
    /**
     * 完成状态.0-执行完成，1-执行中，2:未执行，3:执行被终止，4:暂停
     */
    private Integer isFinish;
    /**
     * 是否录屏：0-否；1-是
     */
    private Integer isRecord;
    /**
     * 执行类型：0立即执行，1指定时间执行，2指定周期执行，3不执行
     */
    private Integer executeType;
    /**
     * 执行时间
     */
    private Date executeTime;

    /**
     * 定时用例和周期用例的Cron表达式
     */
    private String cronTime;

    /**
     * 执行次数
     */
    private Integer executeCount;
    /**
     * 开启下一轮测试：1-开启; 0-不开启
     */
    private Integer isMerge;
    /**
     * 测试结果文件路径
     */
    private String resultFilePath;
    /**
     * 用例排序
     */
    private Integer orderCode;
    /**
     * 成功数
     */
    private Integer successCount;
    /**
     * 失败数
     */
    private Integer failureCount;

    /**
     * 成功率
     */
    private String successRate;

    /**
     * 众测任务id
     */
    private Integer surveyTaskId;

    public Integer getSurveyTaskId() {
        return surveyTaskId;
    }

    public void setSurveyTaskId(Integer surveyTaskId) {
        this.surveyTaskId = surveyTaskId;
    }

    /**
     * 原用例ID
     */
    private Integer oldCaseId;
    private Integer sysTaskId;
    private Date createTime;
    private Date updateTime;
    private Integer createPerson;
    private Integer updatePerson;
    /**
     * 用例手机参数数据
     */
    private List<AppDeviceVO> deviceList;
    /**
     * 执行该用例的设备数
     */
    private Integer deviceNum;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(String caseNum) {
        this.caseNum = caseNum;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
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

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getTaskZipPath() {
        return taskZipPath;
    }

    public void setTaskZipPath(String taskZipPath) {
        this.taskZipPath = taskZipPath;
    }

    public String getZipMd5Code() {
        return zipMd5Code;
    }

    public void setZipMd5Code(String zipMd5Code) {
        this.zipMd5Code = zipMd5Code;
    }

    public Integer getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(Integer isFinish) {
        this.isFinish = isFinish;
    }

    public Integer getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(Integer isRecord) {
        this.isRecord = isRecord;
    }

    public Integer getExecuteType() {
        return executeType;
    }

    public void setExecuteType(Integer executeType) {
        this.executeType = executeType;
    }

    public String getCronTime() {
        return cronTime;
    }

    public void setCronTime(String cronTime) {
        this.cronTime = cronTime;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public Integer getOldCaseId() {
        return oldCaseId;
    }

    public void setOldCaseId(Integer oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public Integer getSysTaskId() {
        return sysTaskId;
    }

    public void setSysTaskId(Integer sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(Integer createPerson) {
        this.createPerson = createPerson;
    }

    public Integer getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(Integer updatePerson) {
        this.updatePerson = updatePerson;
    }

    public List<AppDeviceVO> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<AppDeviceVO> deviceList) {
        this.deviceList = deviceList;
    }

    public Integer getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(Integer deviceNum) {
        this.deviceNum = deviceNum;
    }

    public Integer getIsMerge() {
        return isMerge;
    }

    public void setIsMerge(Integer isMerge) {
        this.isMerge = isMerge;
    }

    public Integer getRealExecuteNum() {
        return realExecuteNum;
    }

    public void setRealExecuteNum(Integer realExecuteNum) {
        this.realExecuteNum = realExecuteNum;
    }
}
