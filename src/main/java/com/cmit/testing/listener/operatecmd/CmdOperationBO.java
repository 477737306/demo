package com.cmit.testing.listener.operatecmd;

public class CmdOperationBO {

    /**
     * proxy的IP和Mac地址拼接成的字符串
     * 192.168.215.63_9E:DA:3E:72:F9:70
     */
    private String proxyIpMac;
    /**
     * 手机设备的唯一序列号
     */
    private String deviceSn;
    /**
     * 命令操作类型
     */
    private String cmdType;
    /**
     * 滑动起始点 X坐标
     */
    private Integer startX;
    /**
     * 滑动结束点 X坐标
     */
    private Integer endX;
    /**
     * 滑动起始点 Y坐标
     */
    private Integer startY;
    /**
     * 滑动结束点 Y坐标
     */
    private Integer endY;
    /**
     * 操作持续时间（滑动默认0.5s）  可选
     */
    private Integer duration;
    /**
     * 输入的文本内容
     */
    private String inputText;
    /**
     * 指定的网址
     */
    private String openUrl;
    /**
     * 短信内容
     */
    private String smsContent;
    /**
     * 电话号码
     */
    private String telNum;
    /**
     * apk应用路径
     */
    private String apkUrl;
    /**
     * 应用包名
     */
    private String pkgName;
    /**
     * 安装应用的版本
     */
    private String apkVersion;
    /**
     * 截图base64字符串数据
     */
    private String base64Str;

    public String getProxyIpMac() {
        return proxyIpMac;
    }

    public void setProxyIpMac(String proxyIpMac) {
        this.proxyIpMac = proxyIpMac;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public Integer getStartX() {
        return startX;
    }

    public void setStartX(Integer startX) {
        this.startX = startX;
    }

    public Integer getEndX() {
        return endX;
    }

    public void setEndX(Integer endX) {
        this.endX = endX;
    }

    public Integer getStartY() {
        return startY;
    }

    public void setStartY(Integer startY) {
        this.startY = startY;
    }

    public Integer getEndY() {
        return endY;
    }

    public void setEndY(Integer endY) {
        this.endY = endY;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getOpenUrl() {
        return openUrl;
    }

    public void setOpenUrl(String openUrl) {
        this.openUrl = openUrl;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getBase64Str() {
        return base64Str;
    }

    public void setBase64Str(String base64Str) {
        this.base64Str = base64Str;
    }
}
