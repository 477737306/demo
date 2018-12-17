package com.cmit.testing.entity.proxy;

import java.util.Date;

/**
 * @author XieZuLiang
 * @description TODO 步骤详情  步骤耗时
 * @date 2018/10/11 0011.
 */
public class StepDetails {
    /**
     * 步骤
     */
    private Integer stepNo;
    /**
     * 动作类型
     */
    private String actionType;
    /**
     * 步骤描述
     */
    private String stepDesc;
    /**
     * 返回结果
     */
    private String resultMsg;
    /**
     * 结果：1-成功；0-失败
     */
    private Integer result;
    /**
     * 开始时间 时间戳
     */
    private Date beginTime;
    /**
     * 结束时间  时间戳
     */
    private Date endTime;
    /**
     * 耗时 单位  ms
     */
    private Integer consumeTime;
    /**
     * 步骤截图
     */
    private String screenShotUrl;

    public Integer getStepNo() {
        return stepNo;
    }

    public void setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
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

    @Override
    public String toString() {
        return "StepDetails{" +
                "stepNo=" + stepNo +
                ", actionType='" + actionType + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", result=" + result +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", consumeTime=" + consumeTime +
                '}';
    }
}
