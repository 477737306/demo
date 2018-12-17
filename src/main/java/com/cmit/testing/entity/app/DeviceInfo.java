package com.cmit.testing.entity.app;

/**
 * @author XieZuLiang
 * @description TODO 设备自动注册信息  JSON对象
 * @date 2018/9/4 0004 下午 4:37.
 */
public class DeviceInfo {

    private String deviceSn;

    private String imei;

    private String brand;

    private String board;

    private String sysOs;
    /**
     * 系统版本
     */
    private String sysOsVersion;

    private String resolution;
    /**
     * SIM对应的IMSI号
     */
    private String imsi;
    /**
     * 手机号码（不一定能获取到）
     */
    private String telNumber;
    /**
     * 屏幕尺寸
     */
    private String screensize;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getSysOs() {
        return sysOs;
    }

    public void setSysOs(String sysOs) {
        this.sysOs = sysOs;
    }

    public String getSysOsVersion() {
        return sysOsVersion;
    }

    public void setSysOsVersion(String sysOsVersion) {
        this.sysOsVersion = sysOsVersion;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getScreensize() {
        return screensize;
    }

    public void setScreensize(String screensize) {
        this.screensize = screensize;
    }
}
