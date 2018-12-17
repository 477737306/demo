package com.cmit.testing.entity.vo;

import com.cmit.testing.security.shiro.ShiroKit;
import com.cmit.testing.security.shiro.ShiroUser;
import org.apache.shiro.SecurityUtils;

import java.util.Date;
import java.util.List;

/**
 * 执行结果表
 */
public class CaseExcResultVO {
    //    业务名称
    private String businessName;
    //项目名称
    private  String projectName;

    //     用例id
    private  Integer caseId;
    //     用例id
    private  Integer testcastId;

    public Integer getTestcastId() {
        return testcastId;
    }

    public void setTestcastId(Integer testcastId) {
        this.testcastId = testcastId;
    }

    //    用例名称
    private String testcastName;

    //      app_case_device表中的ID
    private Integer executeId;

    //    省份
    private String province;

    //    手机号码
    private String phoneNum;
     //    设备id
    private Integer deviceId;


    //    结果表id
    private Integer testCaseReportId;
    //    脚本名称
    private String scriptName;
   //      手机执行品牌
    private String deviceBrand;

    //    执行时间
    private Date executeTime;


    //    TODO 执行人
   private String execMan ;


    //    执行结果
    private Integer excuteResult;

    //    用例状态
    private Integer passStatus;
    //   手机任务 执行状态
    private Integer executeStatus;
    //    日志
    private String log;

    //    缺陷描述
    private String defectDescription;
    private String type;    //报告类型

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //    截图
    private String snapshotLocation;
    //错误截图
    private String errorscreenshot;
    private String excuteResultStr;//导出Excel虚拟字段

    private String excuteTimeStr; //导出Excel虚拟字段

    private String passStatusStr; //导出Excel虚拟字段

    public String getExcuteResultStr() {
        return excuteResultStr;
    }

    public void setExcuteResultStr(String excuteResultStr) {
        this.excuteResultStr = excuteResultStr;
    }

    public String getExcuteTimeStr() {
        return excuteTimeStr;
    }

    public void setExcuteTimeStr(String excuteTimeStr) {
        this.excuteTimeStr = excuteTimeStr;
    }

    public String getPassStatusStr() {
        return passStatusStr;
    }

    public void setPassStatusStr(String passStatusStr) {
        this.passStatusStr = passStatusStr;
    }

    public String getErrorscreenshot() {
        return errorscreenshot;
    }

    public void setErrorscreenshot(String errorscreenshot) {
        this.errorscreenshot = errorscreenshot;
    }

    //    录像
    private String videoLocation;
    //批次
    private Integer executeNum;
    //老id
    private Integer oldCaseId;
    //脚本类型
    private Integer scriptType;
    private List<Integer> testCaseReportIds; //用例报告id集合

    public List<Integer> getTestCaseReportIds() {
        return testCaseReportIds;
    }

    public void setTestCaseReportIds(List<Integer> testCaseReportIds) {
        this.testCaseReportIds = testCaseReportIds;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }
    public Integer getCaseId() {
        return caseId;
    }
    public Integer getExecuteId() {
        return executeId;
    }

    public void setExecuteId(Integer executeId) {
        this.executeId = executeId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getScriptName() {
        return scriptName;
    }

    public void setScriptName(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }
    public Integer getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Integer executeStatus) {
        this.executeStatus = executeStatus;
    }
    public Integer getScriptType() {
        return scriptType;
    }

    public void setScriptType(Integer scriptType) {
        this.scriptType = scriptType;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTestcastName() {
        return testcastName;
    }

    public void setTestcastName(String testcastName) {
        this.testcastName = testcastName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public String getExecMan() {

        return execMan;
    }

    public void setExecMan(String execMan) {

        this.execMan = execMan;
    }

    public Integer getExcuteResult() {
        return excuteResult;
    }

    public void setExcuteResult(Integer excuteResult) {
        this.excuteResult = excuteResult;
    }

    public Integer getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(Integer passStatus) {
        this.passStatus = passStatus;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDefectDescription() {
        return defectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public String getSnapshotLocation() {
        return snapshotLocation;
    }

    public void setSnapshotLocation(String snapshotLocation) {
        this.snapshotLocation = snapshotLocation;
    }

    public String getVideoLocation() {
        return videoLocation;
    }

    public void setVideoLocation(String videoLocation) {
        this.videoLocation = videoLocation;
    }

    public Integer getExecuteNum() {
        return executeNum;
    }

    public void setExecuteNum(Integer executeNum) {
        this.executeNum = executeNum;
    }

    public Integer getOldCaseId() {
        return oldCaseId;
    }

    public void setOldCaseId(Integer oldCaseId) {
        this.oldCaseId = oldCaseId;
    }

    public Integer getTestCaseReportId() {
        return testCaseReportId;
    }

    public void setTestCaseReportId(Integer testCaseReportId) {
        this.testCaseReportId = testCaseReportId;
    }
}
