package com.cmit.testing.entity.vo;

/**
 * @author XieZuLiang
 * @description TODO 用于转化数据库中的数据对应页面的模板格式
 * @date 2018/9/20 0020.
 */
public class AppStepVO {

    /**
     * 动作类型
     */
    private String actionType;
    /**
     * 参数类型：
     * 步骤参数类型(-1:普通文本输入、0:输入账户、1:输入密码；8-动态验证码)
     *
     */
    private String paramType;
    /**
     * 参数key：start、end
     *
     */
    private String paramKey;
    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 定位类型：
     * 0-坐标、1-xpath、3-图像识别、-1-其他
     */
    private String orientType;
    /**
     * 定位的值
     */
    private String orientValue;
    /**
     * 步骤描述
     */
    private String remark;


    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getOrientType() {
        return orientType;
    }

    public void setOrientType(String orientType) {
        this.orientType = orientType;
    }

    public String getOrientValue() {
        return orientValue;
    }

    public void setOrientValue(String orientValue) {
        this.orientValue = orientValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
