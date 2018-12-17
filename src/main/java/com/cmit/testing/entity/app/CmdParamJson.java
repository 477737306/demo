package com.cmit.testing.entity.app;

import com.cmit.testing.entity.proxy.DeviceParam;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO 单个用例下发执行的请求对象
 * @date 2018/9/2 0002 下午 12:36.
 */
public class CmdParamJson {

    private Integer appCaseId;

    private List<DeviceParam> deviceList;

    private String taskZipPath;

    private String md5Code;

    public Integer getAppCaseId() {
        return appCaseId;
    }

    public void setAppCaseId(Integer appCaseId) {
        this.appCaseId = appCaseId;
    }

    public List<DeviceParam> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceParam> deviceList) {
        this.deviceList = deviceList;
    }

    public String getTaskZipPath() {
        return taskZipPath;
    }

    public void setTaskZipPath(String taskZipPath) {
        this.taskZipPath = taskZipPath;
    }

    public String getMd5Code() {
        return md5Code;
    }

    public void setMd5Code(String md5Code) {
        this.md5Code = md5Code;
    }
}
