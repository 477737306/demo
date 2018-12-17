package com.cmit.testing.entity;

import java.util.Date;

/**
 * 用例报告表
 */
public class TestCaseReport {
	private Integer id;
	private String type;// 类型：web,app
	private Integer testcastId;
	private Integer oldTestcaseId;
	private String phoneNum;
	private String province;
	private Integer excuteNum;
	private String log;
	private Integer isFinish;// 完成状态.0:执行完成，1:执行中，2:未执行，3:执行被终止，4:暂停
	private Integer excuteResult; // 执行结果。0-成功，1-失败
	private Integer passStatus;
	private String defectDescription;// 缺陷描述
	private String snapshotLocation;// 截图存放位置
	private String videoLocation;// 视频存放位置
	private String remark;
	private String paramName;
	private Date createTime;
	private String createBy;
	private Date updateTime;
	private String businessName;
	private String testcastName;
	private String updateBy;
	/**
	 * 执行主机
	 */
	private String executeHost;
	/**
	 * 点击次数
	 */
	private Integer clickNum;
	/**
	 * 输入次数
	 */
	private Integer inputNum;
	/**
	 * 用例耗时
	 */
	private Double consumeTime;
	private Integer step; // 脚本步骤
	private String scriptName; // 脚本名称
	private Integer stepResult;// 脚本步骤执行结果 0失败，1成功
	private Date stepBeginTime;// 脚本步骤执行开始时间
	private Date stepEndTime;// 脚本步骤执行结束时间
	private double validateTime; // 获取验证码耗时

	public double getValidateTime() {
		return validateTime;
	}

	public void setValidateTime(double validateTime) {
		this.validateTime = validateTime;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public Integer getStepResult() {
		return stepResult;
	}

	public void setStepResult(Integer stepResult) {
		this.stepResult = stepResult;
	}

	public Date getStepBeginTime() {
		return stepBeginTime;
	}

	public void setStepBeginTime(Date stepBeginTime) {
		this.stepBeginTime = stepBeginTime;
	}

	public Date getStepEndTime() {
		return stepEndTime;
	}

	public void setStepEndTime(Date stepEndTime) {
		this.stepEndTime = stepEndTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTestcastId() {
		return testcastId;
	}

	public void setTestcastId(Integer testcastId) {
		this.testcastId = testcastId;
	}

	public Integer getOldTestcaseId() {
		return oldTestcaseId;
	}

	public void setOldTestcaseId(Integer oldTestcaseId) {
		this.oldTestcaseId = oldTestcaseId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum == null ? null : phoneNum.trim();
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}

	public Integer getExcuteNum() {
		return excuteNum;
	}

	public void setExcuteNum(Integer excuteNum) {
		this.excuteNum = excuteNum;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log == null ? null : log.trim();
	}

	public Integer getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}

	public Integer getExcuteResult() {
		return excuteResult;
	}

	public void setExcuteResult(Integer excuteResult) {
		this.excuteResult = excuteResult;
	}

	public Integer getPassStatus() {
		return passStatus;
	}

	public void setPassStatus(Integer passStatus) {
		this.passStatus = passStatus;
	}

	public String getDefectDescription() {
		return defectDescription;
	}

	public void setDefectDescription(String defectDescription) {
		this.defectDescription = defectDescription == null ? null : defectDescription.trim();
	}

	public String getSnapshotLocation() {
		return snapshotLocation;
	}

	public void setSnapshotLocation(String snapshotLocation) {
		this.snapshotLocation = snapshotLocation == null ? null : snapshotLocation.trim();
	}

	public String getVideoLocation() {
		return videoLocation;
	}

	public void setVideoLocation(String videoLocation) {
		this.videoLocation = videoLocation == null ? null : videoLocation.trim();
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
		this.createBy = createBy == null ? null : createBy.trim();
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
		this.updateBy = updateBy == null ? null : updateBy.trim();
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getTestcastName() {
		return testcastName;
	}

	public void setTestcastName(String testcastName) {
		this.testcastName = testcastName;
	}

	// public Integer getStep() {
	// return step;
	// }
	//
	// public void setStep(Integer step) {
	// this.step = step;
	// }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public Integer getInputNum() {
		return inputNum;
	}

	public void setInputNum(Integer inputNum) {
		this.inputNum = inputNum;
	}

	public Double getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(Double consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getExecuteHost() {
		return executeHost;
	}

	public void setExecuteHost(String executeHost) {
		this.executeHost = executeHost;
	}
}