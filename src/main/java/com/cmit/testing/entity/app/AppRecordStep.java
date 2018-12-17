package com.cmit.testing.entity.app;

import java.io.Serializable;
import java.util.Date;

public class AppRecordStep implements Serializable{

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer caseResultId;

    private Integer stepNo;

    private String stepDesc;

    private String command;

    private String returnInfo;

    private Integer result;

    private Date beginTime;

    private Date endTime;

    private Integer consumeTime;

    private String screenShotUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseResultId() {
        return caseResultId;
    }

    public void setCaseResultId(Integer caseResultId) {
        this.caseResultId = caseResultId;
    }

    public Integer getStepNo() {
        return stepNo;
    }

    public void setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command == null ? null : command.trim();
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo == null ? null : returnInfo.trim();
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Integer consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getScreenShotUrl() {
        return screenShotUrl;
    }

    public void setScreenShotUrl(String screenShotUrl) {
        this.screenShotUrl = screenShotUrl;
    }
}