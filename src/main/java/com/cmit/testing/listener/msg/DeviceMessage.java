package com.cmit.testing.listener.msg;

public class DeviceMessage {

    private String deviceSn;

    /**
     * 消息类型：release-释放设备; occupy-占用设备
     */
    private String type;
    /**
     * 是否被占用：0-空闲；1-占用
     */
    private String useStatus;
    /**
     * 占用设备的用例名称
     */
    private String caseName;
    /**
     * 占用设备的原用例ID
     */
    private String caseId;
    /**
     * 执行进度
     */
    private String executeRate;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getExecuteRate() {
        return executeRate;
    }

    public void setExecuteRate(String executeRate) {
        this.executeRate = executeRate;
    }
}
