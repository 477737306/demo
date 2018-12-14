package com.cmit.testing.entity.vo;

/**
 * 用例执行过程包装类
 * @author dingpeng
 * 2018年10月23日下午3:25:20
 *
 */
public class CaseExecutionProcessVO {
	private Integer testcaseReportId;  //用例报告id
	
	private Integer step; //执行步骤
	
	private String command; //命令
	
	private Integer isFinish; //结果1成功0失败
	
	private String paramName;  //参数中文名
	
	private Integer scriptId;  //脚本id
	
	private Integer Execution; //执行完成标识
	
	private String remark; //备注

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getExecution() {
		return Execution;
	}

	public void setExecution(Integer execution) {
		Execution = execution;
	}

	public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public Integer getTestcaseReportId() {
		return testcaseReportId;
	}

	public void setTestcaseReportId(Integer testcaseReportId) {
		this.testcaseReportId = testcaseReportId;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Integer getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}	
	
	

}
