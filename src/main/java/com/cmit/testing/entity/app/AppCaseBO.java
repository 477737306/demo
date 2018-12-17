package com.cmit.testing.entity.app;

import java.util.List;

public class AppCaseBO {

    private Integer caseId;
    /**
     * 依赖
     */
    private String followId;
    /**
     * 完成状态.0-执行完成，1-执行中，2:未执行，3:执行被终止，4:暂停
     */
    private String isFinish;

    private Integer oldCaseId;

    private List<AppCaseDevice> caseDeviceList;

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public Integer getOldCaseId() {
        return oldCaseId;
    }

    public void setOldCaseId(Integer oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public List<AppCaseDevice> getCaseDeviceList() {
        return caseDeviceList;
    }

    public void setCaseDeviceList(List<AppCaseDevice> caseDeviceList) {
        this.caseDeviceList = caseDeviceList;
    }
}
