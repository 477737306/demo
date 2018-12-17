package com.cmit.testing.entity.vo;

/**
 * @author weiBin
 * @date 2018/9/18
 */
public class SurveyTaskReportVO {
    public static final Integer APP_SOURCE=1;
    public static final Integer WEB_SOURCE=0;
    /**
     * 测试用例Id
     */
    private Integer testcaseId;

    /**
     * 测试用例名称
     */
    private String testcaseName;

    /**
     * 众测任务Id
     */
    private Integer surveyTaskId;
    /**
     * 成功率
     */
    private String successRote;
    /**
     * 结果评分
     */
    private String score;
    /**
     * 平均点击次数
     */
    private String avgClickNum;
    /**
     * 点击次数评分
     */
    private String clickScore;
    /**
     * 平均输入次数
     */
    private String avgInputNum;
    /**
     * 输入次数评分
     */
    private String inputScore;
    /**
     * 平均短信耗时
     */
    private String avgMsgConsumeTime;
    /**
     * 短信耗时评分
     */
    private String msgConsumeTimeScore;
    /**
     * 平均总耗时
     */
    private String avgConsumeTime;
    /**
     * 总耗时评分
     */
    private String consumeTimeScore;

    /**
     * 省名称
     */
    private String province ;

    /**
     * 来源 0 web 1 app
     */
    private Integer source;
    @Override
    public String toString() {
        return "SurveyTaskReportVO{" +
                "testcaseId='" + testcaseId + '\'' +
                ", testcaseName='" + testcaseName + '\'' +
                ", successRote='" + successRote + '\'' +
                ", score='" + score + '\'' +
                ", avgClickNum='" + avgClickNum + '\'' +
                ", clickScore='" + clickScore + '\'' +
                ", avgInputNum='" + avgInputNum + '\'' +
                ", inputScore='" + inputScore + '\'' +
                ", avgMsgConsumeTime='" + avgMsgConsumeTime + '\'' +
                ", msgConsumeTimeScore='" + msgConsumeTimeScore + '\'' +
                ", avgConsumeTime='" + avgConsumeTime + '\'' +
                ", consumeTimeScore='" + consumeTimeScore + '\'' +
                ", province='" + province + '\'' +
                '}';
    }

    public String getSuccessRote() {
        return successRote;
    }

    public void setSuccessRote(String successRote) {
        this.successRote = successRote;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAvgClickNum() {
        return avgClickNum;
    }

    public void setAvgClickNum(String avgClickNum) {
        this.avgClickNum = avgClickNum;
    }

    public String getClickScore() {
        return clickScore;
    }

    public void setClickScore(String clickScore) {
        this.clickScore = clickScore;
    }

    public String getAvgInputNum() {
        return avgInputNum;
    }

    public void setAvgInputNum(String avgInputNum) {
        this.avgInputNum = avgInputNum;
    }

    public String getInputScore() {
        return inputScore;
    }

    public void setInputScore(String inputScore) {
        this.inputScore = inputScore;
    }

    public String getAvgMsgConsumeTime() {
        return avgMsgConsumeTime;
    }

    public void setAvgMsgConsumeTime(String avgMsgConsumeTime) {
        this.avgMsgConsumeTime = avgMsgConsumeTime;
    }

    public String getMsgConsumeTimeScore() {
        return msgConsumeTimeScore;
    }

    public void setMsgConsumeTimeScore(String msgConsumeTimeScore) {
        this.msgConsumeTimeScore = msgConsumeTimeScore;
    }

    public String getAvgConsumeTime() {
        return avgConsumeTime;
    }

    public void setAvgConsumeTime(String avgConsumeTime) {
        this.avgConsumeTime = avgConsumeTime;
    }

    public String getConsumeTimeScore() {
        return consumeTimeScore;
    }

    public void setConsumeTimeScore(String consumeTimeScore) {
        this.consumeTimeScore = consumeTimeScore;
    }

    public Integer getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(Integer testCaseId) {
        this.testcaseId = testCaseId;
    }

    public String getTestcaseName() {
        return testcaseName;
    }

    public void setTestcaseName(String testCaseName) {
        this.testcaseName = testCaseName;
    }

    public Integer getSurveyTaskId() {
        return surveyTaskId;
    }

    public void setSurveyTaskId(Integer surveyTaskId) {
        this.surveyTaskId = surveyTaskId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}
