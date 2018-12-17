package com.cmit.testing.entity;

import java.util.Date;

public class Business {
	private Integer id;

	private Integer projectId;

	private String name;

	private Integer status;

	private Date createTime;

	private String createBy;

	private Date updateTime;

	private String updateBy;

	private Integer successNum;

	private Integer failureNum;

	private Double successRate;

	private Integer code;

	private Integer isFinish;

	private Integer sysTaskId;// 任务id

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}

	public Integer getSysTaskId() {
		return sysTaskId;
	}

	public void setSysTaskId(Integer sysTaskId) {
		this.sysTaskId = sysTaskId;
	}

	@Override
	public String toString() {
		return "Business [id=" + id + ", projectId=" + projectId + ", name=" + name + ", status=" + status
				+ ", createTime=" + createTime + ", createBy=" + createBy + ", updateTime=" + updateTime + ", updateBy="
				+ updateBy + ", successNum=" + successNum + ", failureNum=" + failureNum + ", successRate="
				+ successRate + ", code=" + code + ", isFinish=" + isFinish + ", sysTaskId=" + sysTaskId + "]";
	}
}