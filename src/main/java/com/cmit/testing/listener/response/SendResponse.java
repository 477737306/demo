package com.cmit.testing.listener.response;

/**
 * 短信发送的响应对象
 */
public class SendResponse {

    private String deviceSn;

    /**
     * 0-短信发送成功；1-短信发送失败；2-设备不存在或无法获取
     */
    private Integer result;

    private String msg;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
