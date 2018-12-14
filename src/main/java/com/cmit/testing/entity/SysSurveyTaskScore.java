package com.cmit.testing.entity;

import com.cmit.testing.utils.StringUtils;

import java.math.BigDecimal;

/**
 * @author weiBin
 * @date 2018/9/19
 */
public class SysSurveyTaskScore {
    private Integer id;
    /**
     * 众测任务Id
     */
    private Integer surveyTaskId;
    /**
     * 用例副本id
     */
    private Integer testcaseId;
    /**
     * 类型 1 业务耗时 2 点击次数 3 输入次数 4 短信送达耗时
     */
    private String type;
    /**
     * 起始值
     */
    private String start;
    /**
     * 终止值
     */
    private String end;
    /**
     * 评分
     */
    private String score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSurveyTaskId() {
        return surveyTaskId;
    }

    public void setSurveyTaskId(Integer surveyTaskId) {
        this.surveyTaskId = surveyTaskId;
    }

    public Integer getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(Integer testcaseId) {
        this.testcaseId = testcaseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart() {
        if (StringUtils.isEmpty(start)) {
            return null;
        } else {
            return start;
        }
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        if (StringUtils.isEmpty(end)) {
            return null;
        } else {
            return end;
        }
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getScore() {
        if (StringUtils.isEmpty(score)) {
            return null;
        } else {
            return score;
        }
    }

    public void setScore(String score) {
        this.score = score;
    }
}
