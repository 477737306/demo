package com.cmit.testing.utils;

/**
 * @author weiBin
 * @date 2018/9/19
 */
public enum SurveyTaskScoreTypeEnum {
    //1 业务耗时 2 点击次数 3 输入次数 4 短信送达耗时
    CASE_TIME("1", "业务耗时"),
    CLICK_NUM("2", "点击次数"),
    INPUT_NUM("3", "输入次数"),
    MSG_TIME("4", "短信送达耗时"),;
    private String code;
    private String message;

    public static SurveyTaskScoreTypeEnum getSurveyTaskScoreTypeEnumCode(String code) {
        for (SurveyTaskScoreTypeEnum scoreTypeEnum : SurveyTaskScoreTypeEnum.values()) {
            if (scoreTypeEnum.getCode().equals(code)) {
                return scoreTypeEnum;
            }
        }
        return null;
    }

    SurveyTaskScoreTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
