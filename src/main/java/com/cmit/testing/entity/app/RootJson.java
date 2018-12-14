package com.cmit.testing.entity.app;

import com.alibaba.fastjson.annotation.JSONField;
import com.cmit.testing.entity.proxy.CmdParamStartTask;

/**
 * @author XieZuLiang
 * @description TODO 下发信息给Proxy的实体对象
 * @date 2018/9/2 0002 下午 12:33.
 */
public class RootJson {

    private String ACC_TYPE;

    private String SEQ_NO;

    private String CMD_TYPE;

    private CmdParamJson cmdParamJson;

    private CmdParamStartTask CMD_PARAM;

    public CmdParamStartTask getCMD_PARAM() {
        return CMD_PARAM;
    }

    public void setCMD_PARAM(CmdParamStartTask CMD_PARAM) {
        this.CMD_PARAM = CMD_PARAM;
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
    public CmdParamJson getCmdParamJson() {
        return cmdParamJson;
    }

    public void setCmdParamJson(CmdParamJson cmdParamJson) {
        this.cmdParamJson = cmdParamJson;
    }

    @Override
    public String toString() {
        return "RootJson{" +
                "ACC_TYPE='" + ACC_TYPE + '\'' +
                ", SEQ_NO='" + SEQ_NO + '\'' +
                ", CMD_TYPE='" + CMD_TYPE + '\'' +
                ", CMD_PARAM=" + cmdParamJson +
                '}';
    }
}
