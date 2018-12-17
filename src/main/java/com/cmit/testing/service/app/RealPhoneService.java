package com.cmit.testing.service.app;

import com.cmit.testing.entity.proxy.CmdBody;
import com.cmit.testing.listener.operatecmd.CmdOperationBO;

import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/9/13.
 */
public interface RealPhoneService {

    /**
     * 发送远程操控指令
     * @param cbo
     */
    void sendCmd(CmdOperationBO cbo);

    /**
     * 开启远程操控
     */
    void showRealPhone(String deviceSn, String proxyIp, String proxyMac);

}
