package com.cmit.testing.entity.proxy;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author XieZuLiang
 * @description TODO 请求命令头
 * @date 2018/9/5 0005 上午 10:22.
 */
public class CmdRoot {
    private String ACC_TYPE;

    private String SEQ_NO;

    private String CMD_TYPE;

    private Object CMD_PARAM;

    @JSONField(name = "ACC_TYPE")
    public String getACC_TYPE() {
        return ACC_TYPE;
    }

    public void setACC_TYPE(String ACC_TYPE) {
        this.ACC_TYPE = ACC_TYPE;
    }

    @JSONField(name = "SEQ_NO")
    public String getSEQ_NO() {
        return SEQ_NO;
    }

    public void setSEQ_NO(String SEQ_NO) {
        this.SEQ_NO = SEQ_NO;
    }

    @JSONField(name = "CMD_TYPE")
    public String getCMD_TYPE() {
        return CMD_TYPE;
    }

    public void setCMD_TYPE(String CMD_TYPE) {
        this.CMD_TYPE = CMD_TYPE;
    }

    @JSONField(name = "CMD_PARAM")
    public Object getCMD_PARAM() {
        return CMD_PARAM;
    }

    public void setCMD_PARAM(Object CMD_PARAM) {
        this.CMD_PARAM = CMD_PARAM;
    }
}
