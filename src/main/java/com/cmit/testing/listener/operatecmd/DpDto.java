package com.cmit.testing.listener.operatecmd;

/**
 * proxy和device的传输对象
 */
public class DpDto {

    private Integer deviceId;

    private String deviceSn;

    private String telNum;

    private Integer proxyId;

    private String proxyIp;

    private String proxyMac;

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public Integer getProxyId() {
        return proxyId;
    }

    public void setProxyId(Integer proxyId) {
        this.proxyId = proxyId;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public String getProxyMac() {
        return proxyMac;
    }

    public void setProxyMac(String proxyMac) {
        this.proxyMac = proxyMac;
    }
}
