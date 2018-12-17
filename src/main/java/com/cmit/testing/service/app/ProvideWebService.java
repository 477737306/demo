package com.cmit.testing.service.app;

import com.cmit.testing.entity.SendMessage;
import com.cmit.testing.entity.app.AppCaseDevice;

import java.util.List;

/**
 * 提供给web侧使用的接口
 */
public interface ProvideWebService {

    /**
     * 向指定的手机设备下发发送短信命令
     * @param sendMessage 要发送的短信相关信息
     */
    public void sendMessageTaskToProxy(SendMessage sendMessage);

    /**
     * 根据依赖ID获取对应执行通过的手机号码
     * @param followIds
     * @return
     */
    public List<String> getPhoneNumByFollow(String followIds);

}
