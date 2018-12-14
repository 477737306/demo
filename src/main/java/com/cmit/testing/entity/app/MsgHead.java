package com.cmit.testing.entity.app;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/4 0004 下午 3:41.
 */
public class MsgHead<T> {

    private String ACC_TYPE;

    private String SEQ_NO;

    private String CMD_TYPE;

    private T CMD_PARAM;

    public MsgHead(){}

    public MsgHead(String ACC_TYPE,String CMD_TYPE){
        this.ACC_TYPE = ACC_TYPE;
        this.CMD_TYPE = CMD_TYPE;
        this.SEQ_NO = System.currentTimeMillis() + "";
    }

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
    public T getCMD_PARAM() {
        return CMD_PARAM;
    }

    public void setCMD_PARAM(T CMD_PARAM) {
        this.CMD_PARAM = CMD_PARAM;
    }
}
