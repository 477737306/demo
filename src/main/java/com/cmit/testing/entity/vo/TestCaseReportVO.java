package com.cmit.testing.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.Date;
import java.util.List;

/**
 * 用例报告包装类
 *
 * @author YangWanLi
 * @date 2018/9/19 19:15
 */
public class TestCaseReportVO {

    private String projectName; //项目名称

    private Integer testCaseReportId;   //报告id

    /**
     * 截图打包下载使用时：app 表示只下载app相关的截图   web  表示只下载web相关的截图
     */
    private String type;    //报告类型

    private Integer testcastId;  //用例id

    private String serialNumber;  //用例编号

    private String testcastName;    //用例名称

    private String businessName;    //业务名称

    private String phoneNum;    //手机号

    private String province;    //省份

    private Date excuteTime;    //执行时间

    private Integer excuteResult; //0失败，1成功

    private Integer excuteNum;  //批次号

    private Integer passStatus; //用例状态

    private String log; //日志

    private String defectDescription;   //缺陷描述

    private String snapshotLocation;    //截图存放位置

    private String videoLocation;   //视频存放位置

    private String executorName; //执行人

    private String isFinish;  //执行状态
    

	/**
     * 执行主机
     */
    private String executeHost;
    @JsonIgnore
    private List<Integer> testcastIds;  //用例id集合


    @JsonIgnore
    private Integer surveyTaskId;   //任务id
    
    private String excuteResultStr;//导出Excel虚拟字段
    
    private String excuteTimeStr; //导出Excel虚拟字段
    
    private String passStatusStr; //导出Excel虚拟字段
    
    private List<Integer> webTestCaseReportIds; //web用例报告id集合(导出Excel虚拟字段)
    private List<Integer> appTestCaseReportIds; //app用例报告id集合(导出Excel虚拟字段)


    public List<Integer> getWebTestCaseReportIds() {
        return webTestCaseReportIds;
    }

    public void setWebTestCaseReportIds(List<Integer> webTestCaseReportIds) {
        this.webTestCaseReportIds = webTestCaseReportIds;
    }

    public List<Integer> getAppTestCaseReportIds() {
        return appTestCaseReportIds;
    }

    public void setAppTestCaseReportIds(List<Integer> appTestCaseReportIds) {
        this.appTestCaseReportIds = appTestCaseReportIds;
    }

    public String getExcuteResultStr() {
		return excuteResultStr;
	}

	public void setExcuteResultStr(String excuteResultStr) {
		this.excuteResultStr = excuteResultStr;
	}

	public String getExcuteTimeStr() {
		return excuteTimeStr;
	}

	public void setExcuteTimeStr(String excuteTimeStr) {
		this.excuteTimeStr = excuteTimeStr;
	}

	public String getPassStatusStr() {
		return passStatusStr;
	}

	public void setPassStatusStr(String passStatusStr) {
		this.passStatusStr = passStatusStr;
	}

	public List<Integer> getTestcastIds() {
        return testcastIds;
    }

    public void setTestcastIds(List<Integer> testcastIds) {
        this.testcastIds = testcastIds;
    }

    public Integer getTestCaseReportId() {
        return testCaseReportId;
    }

    public void setTestCaseReportId(Integer testCaseReportId) {
        this.testCaseReportId = testCaseReportId;
    }

    public Integer getTestcastId() {
        return testcastId;
    }

    public void setTestcastId(Integer testcastId) {
        this.testcastId = testcastId;
    }

    public String getTestcastName() {
        return testcastName;
    }

    public void setTestcastName(String testcastName) {
        this.testcastName = testcastName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getExcuteResult() {
        return excuteResult;
    }

    public void setExcuteResult(Integer excuteResult) {
        this.excuteResult = excuteResult;
    }

    public Integer getExcuteNum() {
        return excuteNum;
    }

    public void setExcuteNum(Integer excuteNum) {
        this.excuteNum = excuteNum;
    }

    public Integer getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(Integer passStatus) {
        this.passStatus = passStatus;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDefectDescription() {
        return defectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public String getSnapshotLocation() {
        return snapshotLocation;
    }

    public void setSnapshotLocation(String snapshotLocation) {
        this.snapshotLocation = snapshotLocation;
    }

    public String getVideoLocation() {
        return videoLocation;
    }

    public void setVideoLocation(String videoLocation) {
        this.videoLocation = videoLocation;
    }

    public Date getExcuteTime() {
        return excuteTime;
    }

    public void setExcuteTime(Date excuteTime) {
        this.excuteTime = excuteTime;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getExecuteHost() {
        return executeHost;
    }

    public void setExecuteHost(String executeHost) {
        this.executeHost = executeHost;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
