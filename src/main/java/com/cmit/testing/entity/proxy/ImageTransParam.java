package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description TODO 图像传输接口参数
 * @date 2018/9/5 0005 上午 10:59.
 */
public class ImageTransParam {

    /**
     * 需要获取图像流手机的唯一序列号
     */
    private String deviceSn;

    /**
     * 0-只看手机界面不进行控制  1-需要远程控制手机
     */
    private int control;

    /**
     * 图像尺寸压缩比，默认为2，取值需要大于1
     */
    private int compressRate;

    /**
     * 图像传输socket的端口号
     */
    private int image_socketPort;

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public int getCompressRate() {
        return compressRate;
    }

    public void setCompressRate(int compressRate) {
        this.compressRate = compressRate;
    }

    public int getImage_socketPort() {
        return image_socketPort;
    }

    public void setImage_socketPort(int image_socketPort) {
        this.image_socketPort = image_socketPort;
    }
}
