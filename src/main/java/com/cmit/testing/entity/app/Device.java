package com.cmit.testing.entity.app;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangxiaofang on 2018/8/28.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 终端唯一序列号
     */
    private String deviceSn;
    /**
     * 终端编码
     */
    private String deviceCode;

    private String brand;
    /**
     * 手机型号
     */
    private String version;

    /**
     * 操作系统+版本号
     */
    private String sysOs;

    private String resolution;

    /**
     * 预览图片路径
     */
    private String previewUrl;

    private String imei1;

    private String imei2;

    /**
     * 移动设备识别码
     */
    private String meid;
    /**
     * SIM对应的IMSI号
     */
    private String imsi;
    /**
     * imsi是否设置为空
     * imsiSetNull 若是不为空，则imsi设值为Null
     */
    private String imsiSetNull;
    /**
     * 手机号
     */
    private String telNum;

    private String province;

    private String useStatus;

    private String onlineStatus;

    private Integer deptId;

    /**
     * 归属人
     */
    private String belonger;

    private Date createTime;

    private Integer createPerson;

    private Date updateTime;
    private Integer updatePerson;

    // add 展示给页面
    private String createPersonName;
    private String updatePersonName;

    private Integer proxyId;

    // add proxy info
    private String proxyIp;

    private String proxyMac;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSysOs() {
        return sysOs;
    }

    public void setSysOs(String sysOs) {
        this.sysOs = sysOs;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getImei1() {
        return imei1;
    }

    public void setImei1(String imei1) {
        this.imei1 = imei1;
    }

    public String getImei2() {
        return imei2;
    }

    public void setImei2(String imei2) {
        this.imei2 = imei2;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getBelonger() {
        return belonger;
    }

    public void setBelonger(String belonger) {
        this.belonger = belonger;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(Integer createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(Integer updatePerson) {
        this.updatePerson = updatePerson;
    }

    public Integer getProxyId() {
        return proxyId;
    }

    public void setProxyId(Integer proxyId) {
        this.proxyId = proxyId;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImsiSetNull() {
        return imsiSetNull;
    }

    public void setImsiSetNull(String imsiSetNull) {
        this.imsiSetNull = imsiSetNull;
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

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getUpdatePersonName() {
        return updatePersonName;
    }

    public void setUpdatePersonName(String updatePersonName) {
        this.updatePersonName = updatePersonName;
    }
}
