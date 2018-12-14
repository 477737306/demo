package com.cmit.testing.entity.app;

import java.util.List;

public class DeviceDTO {

    private String deviceSn;

    private String useStatus;

    private String preOccupancy;

    private String onlineStatus;

    private String deviceId;

    private List<Integer> deviceList;

    private List<String> deviceSnList;

    private String telNum;

    private String proxyId;


    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getProxyId() {
        return proxyId;
    }

    public void setProxyId(String proxyId) {
        this.proxyId = proxyId;
    }

    public List<Integer> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<Integer> deviceList) {
        this.deviceList = deviceList;
    }

    public String getPreOccupancy() {
        return preOccupancy;
    }

    public void setPreOccupancy(String preOccupancy) {
        this.preOccupancy = preOccupancy;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public List<String> getDeviceSnList() {
        return deviceSnList;
    }

    public void setDeviceSnList(List<String> deviceSnList) {
        this.deviceSnList = deviceSnList;
    }
}
