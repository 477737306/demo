package com.cmit.testing.entity.proxy;

import java.util.List;

/**
 * @author XieZuLiang
 * @description TODO 心跳信息对象
 * @date 2018/9/11 0011.
 */
public class BeartHeatInfo {

    private List<String> online;

    private List<String> offline;

    public List<String> getOnline() {
        return online;
    }

    public void setOnline(List<String> online) {
        this.online = online;
    }

    public List<String> getOffline() {
        return offline;
    }

    public void setOffline(List<String> offline) {
        this.offline = offline;
    }
}
