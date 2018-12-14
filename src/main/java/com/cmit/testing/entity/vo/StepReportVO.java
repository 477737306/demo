package com.cmit.testing.entity.vo;

/**
 * @author weiBin
 * @date 2018/9/18
 */
public class StepReportVO {
    /**
     * 步骤
     */
    private String step ;
    /**
     * 步骤命令
     */
    private String command;
    /**
     * 步骤参数
     */
    private String paramName;
    /**
     * 步骤描述
     */
    private String remark;
    /**
     * 平均耗时
     */
    private String avgConsumeTime ;

    /**
     *
     * 步骤截图 app
     */
    private String screenshotUrl;

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    @Override
    public String toString() {
        return "StepReportVO{" +
                "step='" + step + '\'' +
                ", command='" + command + '\'' +
                ", paramName='" + paramName + '\'' +
                ", remark='" + remark + '\'' +
                ", avgConsumeTime='" + avgConsumeTime + '\'' +
                '}';
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
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

    public String getAvgConsumeTime() {
        return avgConsumeTime;
    }

    public void setAvgConsumeTime(String avgConsumeTime) {
        this.avgConsumeTime = avgConsumeTime;
    }
}
