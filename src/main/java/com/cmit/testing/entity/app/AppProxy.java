package com.cmit.testing.entity.app;

import java.util.Date;

public class AppProxy {
    /**
     * Proxy唯一编号
     */
    private Integer proxyId;

    /**
     * Proxy的IP地址
     */
    private String proxyIp;

    /**
     * Proxy的Mac地址
     */
    private String proxyMac;

    /**
     * 在线状态：0-在线；1-离线；2-下线
     */
    private String onlineStatus;

    private Date updateTime;

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
        this.proxyIp = proxyIp == null ? null : proxyIp.trim();
    }

    public String getProxyMac() {
        return proxyMac;
    }

    public void setProxyMac(String proxyMac) {
        this.proxyMac = proxyMac == null ? null : proxyMac.trim();
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }
    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus == null ? null : onlineStatus.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}