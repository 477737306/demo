package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description 手机屏幕操作事件
 * @date 2018/9/5 0005 上午 10:23.
 */
public class CmdBody {
    /**
     * 终端手机唯一序列号
     */
    private String deviceSn;

    /**
     * 横坐标
     */
    private Integer x;

    /**
     * 纵坐标
     */
    private Integer y;

    /**
     * 滑动起始点横坐标
     */
    private Integer fromX;

    /**
     * 滑动起始点纵坐标
     */
    private Integer fromY;

    /**
     * 滑动结束点横坐标
     */
    private Integer toX;

    /**
     * 滑动借宿点纵坐标
     */
    private Integer toY;

    /**
     * 操作持续时间（滑动默认0.5s）  可选
     */
    private Integer duration;

    /**
     * 执行搜索/打开网址/输入文本等操作时传递的文本内容
     */
    private String text;

    /**
     * 发送短信的接收者
     */
    private String smsRecipient;

    /**
     * 发送短信的内容
     */
    private String smsContent;

    /**
     * 打开网页网址
     */
    private String url;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getFromX() {
        return fromX;
    }

    public void setFromX(Integer fromX) {
        this.fromX = fromX;
    }

    public Integer getFromY() {
        return fromY;
    }

    public void setFromY(Integer fromY) {
        this.fromY = fromY;
    }

    public Integer getToX() {
        return toX;
    }

    public void setToX(Integer toX) {
        this.toX = toX;
    }

    public Integer getToY() {
        return toY;
    }

    public void setToY(Integer toY) {
        this.toY = toY;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSmsRecipient() {
        return smsRecipient;
    }

    public void setSmsRecipient(String smsRecipient) {
        this.smsRecipient = smsRecipient;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
