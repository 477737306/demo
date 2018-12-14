package com.cmit.testing.entity.proxy;

import java.util.Date;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/10/13 0013.
 */
public class VerifySmsMsg {

    /**
     * 设备唯一标识
     */
    private String deviceSn;
    /**
     * 手机IMEI号
     */
    private String imei;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 手机号码
     */
    private String number;

    /**
     * 接收短信的时间
     */
    private Date receiveSmsTime;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getReceiveSmsTime() {
        return receiveSmsTime;
    }

    public void setReceiveSmsTime(Date receiveSmsTime) {
        this.receiveSmsTime = receiveSmsTime;
    }

    @Override
    public String toString() {
        return "VerifySmsMsg{" +
                "deviceSn='" + deviceSn + '\'' +
                ", imei='" + imei + '\'' +
                ", content='" + content + '\'' +
                ", number=" + number +
                '}';
    }
}
