package com.cmit.testing.entity;

public class Parameter {
    private Integer id;

    private Integer testcaseId;

    private Integer multiFlag;

    private String param;

    private String province;


    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(Integer testcaseId) {
        this.testcaseId = testcaseId;
    }

    public Integer getMultiFlag() {
        return multiFlag;
    }

    public void setMultiFlag(Integer multiFlag) {
        this.multiFlag = multiFlag;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param == null ? null : param.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}