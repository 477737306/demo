package com.cmit.testing.entity.vo;

/**
 * @author weiBin
 * @date 2018/9/19
 */
public class SurveyTaskScoreItemVO {
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

    public SurveyTaskScoreItemVO(String start, String end, String score) {
        this.start = start;
        this.end = end;
        this.score = score;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
