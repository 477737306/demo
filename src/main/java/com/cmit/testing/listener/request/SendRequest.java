package com.cmit.testing.listener.request;

/**
 * 短信发送请求体对象
 */
public class SendRequest {

    /**
     * 短信发送发设备序列号
     */
    private String deviceSn;

    /**
     * 短信接收方号码
     */
    private String phoneNumber;

    /**
     * 短信内容
     */
    private String smsContent;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }
}
