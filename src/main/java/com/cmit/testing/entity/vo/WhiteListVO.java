package com.cmit.testing.entity.vo;

import java.io.Serializable;

/**
 * @author: create by YangWanLi
 * @version: v1.0
 * @description: 白名单
 * @date:2018/12/11
 */
public class WhiteListVO implements Serializable {

    private String id;

    private String ip; //允许访问Ip

    private String remark; //备注

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public WhiteListVO( String id ,String ip, String remark) {
        this.id = id;
        this.ip = ip;
        this.remark = remark;
    }

    public WhiteListVO() {
    }
}
