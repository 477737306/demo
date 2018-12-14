package com.cmit.testing.entity.vo;


/**
 * 执行步骤包装类
 *
 * @author YangWanLi
 * @date 2018/9/19 19:15
 */
public class RecordStepVO {

    private Integer testCaseReportId;   //报告id

    private String testcastName;    //用例名称

    private String scriptName; //脚本名称

    private String type;    //脚本类型

    private String phoneNum;    //手机号

    private Integer step; //执行步骤

    private Integer excuteNum;  //批次号

    private Integer consumeTime;  //步骤耗时毫秒

    private Integer stepResult;//脚本步骤执行结果 0失败，1成功

    private String paramName; //参数中文名

    private String remark;  //描述

    private String screenShotUrl;//步骤截图

    public String getScreenShotUrl() {
        return screenShotUrl;
    }

    public void setScreenShotUrl(String screenShotUrl) {
        this.screenShotUrl = screenShotUrl;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTestCaseReportId() {
        return testCaseReportId;
    }

    public void setTestCaseReportId(Integer testCaseReportId) {
        this.testCaseReportId = testCaseReportId;
    }

    public String getTestcastName() {
        return testcastName;
    }

    public void setTestcastName(String testcastName) {
        this.testcastName = testcastName;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(Integer consumeTime) {
        this.consumeTime = consumeTime;
    }

    public Integer getStepResult() {
        return stepResult;
    }

    public void setStepResult(Integer stepResult) {
        this.stepResult = stepResult;
    }

    public Integer getExcuteNum() {
        return excuteNum;
    }

    public void setExcuteNum(Integer excuteNum) {
        this.excuteNum = excuteNum;
    }
}