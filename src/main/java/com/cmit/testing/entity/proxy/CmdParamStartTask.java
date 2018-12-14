package com.cmit.testing.entity.proxy;

/**
 * @author XieZuLiang
 * @description TODO 下发用例后，Proxy响应的信息对象
 * @date 2018/9/16 0016.
 */
public class CmdParamStartTask {

    private Integer appCaseId;

    /**
     * 0-成功；1-下载任务zip包异常；2-任务zip包解析异常；
     * 3-任务Zip包文件MD5校验异常；4-设备状态异常；5-其他异常
     */
    private Integer result;

    private String msg;

    public Integer getAppCaseId() {
        return appCaseId;
    }

    public void setAppCaseId(Integer appCaseId) {
        this.appCaseId = appCaseId;
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
