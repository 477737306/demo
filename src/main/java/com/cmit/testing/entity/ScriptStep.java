package com.cmit.testing.entity;
// add by xxm 8.5
public class ScriptStep implements Cloneable {
    private Integer id;

    private Integer scriptId;

    private Integer step;

    private String command;

    private String paramName;

    private String paramKey;

    private String paramValue;

    private Integer paramType;

    private String orientType;

    private String orientValue;

    private String remark;
    
    private String matchKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScriptId() {
        return scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command == null ? null : command.trim();
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName == null ? null : paramName.trim();
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey == null ? null : paramKey.trim();
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue == null ? null : paramValue.trim();
    }

    public Integer getParamType() {
        return paramType;
    }

    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }

    public String getOrientType() {
        return orientType;
    }

    public void setOrientType(String orientType) {
        this.orientType = orientType == null ? null : orientType.trim();
    }

    public String getOrientValue() {
        return orientValue;
    }

    public void setOrientValue(String orientValue) {
        this.orientValue = orientValue == null ? null : orientValue.trim();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getMatchKey() {
		return matchKey;
	}

	public void setMatchKey(String matchKey) {
		this.matchKey = matchKey;
	}
}