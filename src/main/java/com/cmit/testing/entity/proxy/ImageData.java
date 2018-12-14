package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description 图像数据对象
 * @date 2018/9/11 0011.
 */
public class ImageData implements Comparable<ImageData>{

    private String deviceSn;
    /**
     * 图片标识
     */
    private long imgMarkKey;

    private long totalPart ;// 总份数

    private long receivedParts ;//已接收分数

    private long currentPart ;//当前partnumber

    private long dataLength;// 数据总长度

    private boolean badData;//数据是否出现异常或有丢失。

    private byte[] data;

    private int currentPosition;

    public ImageData(String deviceSn,long part,long imgMarkKey)
    {
        this.deviceSn = deviceSn;
        this.totalPart = part;
        this.imgMarkKey = imgMarkKey;
        this.data = new byte[(int)part*1024];
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public long getTotalPart() {
        return totalPart;
    }


    public long getDataLength() {
        return dataLength;
    }

    public void setDataLength(long dataLength) {
        this.dataLength += dataLength;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public long getReceivedParts() {
        return receivedParts;
    }


    public long getNextPart() {
        return ++currentPart;
    }

    public void setCurrentPart(long currentPart) {
        this.currentPart = currentPart;
        this.receivedParts++;
    }

    public boolean receivedAll(){
        return totalPart == receivedParts;
    }

    public boolean isBadData() {
        return badData;
    }

    public void badData() {
        this.badData = true;
    }

    public long getImgMarkKey() {
        return imgMarkKey;
    }

    public void setImgMarkKey(long imgMarkKey) {
        this.imgMarkKey = imgMarkKey;
    }

    @Override
    public int compareTo(ImageData o)
    {
        if(o.imgMarkKey>this.imgMarkKey)
        {
            return 1;
        }
        if(o.imgMarkKey==this.imgMarkKey)
        {
            return 0;
        }
        else
        {
            return -1;
        }

    }
}

