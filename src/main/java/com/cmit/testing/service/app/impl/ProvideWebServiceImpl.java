package com.cmit.testing.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.common.syncmsg.SyncFuture;
import com.cmit.testing.dao.TestCaseReportMapper;
import com.cmit.testing.dao.app.AppCaseDeviceMapper;
import com.cmit.testing.dao.app.AppCaseMapper;
import com.cmit.testing.dao.app.DeviceMapper;
import com.cmit.testing.entity.SendMessage;
import com.cmit.testing.entity.app.AppCase;
import com.cmit.testing.entity.app.AppCaseBO;
import com.cmit.testing.entity.app.AppCaseDevice;
import com.cmit.testing.entity.app.Device;
import com.cmit.testing.entity.proxy.CmdRoot;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.listener.operatecmd.DpDto;
import com.cmit.testing.listener.operatecmd.OperateCmdFactory;
import com.cmit.testing.listener.proxy.netty.NettyChannelMap;
import com.cmit.testing.listener.proxy.netty.NettyServerHandler;
import com.cmit.testing.listener.request.SendRequest;
import com.cmit.testing.listener.response.SendResponse;
import com.cmit.testing.service.SendMessageTaskService;
import com.cmit.testing.service.app.AppCaseDeviceService;
import com.cmit.testing.service.app.ProvideWebService;
import com.cmit.testing.utils.RedisUtil;
import com.cmit.testing.utils.StringUtils;
import io.netty.channel.Channel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("provideWebService")
public class ProvideWebServiceImpl implements ProvideWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvideWebServiceImpl.class);

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private SendMessageTaskService sendMessageTaskService;
    @Autowired
    private AppCaseMapper appCaseMapper;
    @Autowired
    private TestCaseReportMapper testCaseReportMapper;
    @Autowired
    private AppCaseDeviceService appCaseDeviceService;

    @Override
    public List<String> getPhoneNumByFollow(String followIds) {
        Map<String, List<Integer>> followMap = (Map<String, List<Integer>>) JSONObject.parse(followIds);
        List<Integer> appList = followMap.get("app");
        List<Integer> webList = followMap.get("web");

        List<String> phoneList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(webList))
        {
            // 获取依赖web用例关联的手机号码
            for (Integer webCaseId : webList)
            {
                List<String> phoneNumList = testCaseReportMapper.getWebPhoneNumList(webCaseId, 0);
                if (CollectionUtils.isNotEmpty(phoneNumList))
                {
                    if (CollectionUtils.isEmpty(phoneList))
                    {
                        phoneList = phoneNumList;
                    }
                    else
                    {
                        phoneList.retainAll(phoneNumList);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(appList))
        {
            for (Integer id : appList)
            {
                List<AppCaseDevice> list = appCaseDeviceService.getCaseDeviceList(id);
                if (CollectionUtils.isNotEmpty(list))
                {
                    List<String> appPhoneList = new ArrayList<>();
                    for (AppCaseDevice cd : list)
                    {
                        if (appPhoneList.contains(cd.getTelNum()))
                        {
                            continue;
                        }
                        appPhoneList.add(cd.getTelNum());
                    }

                    if (CollectionUtils.isEmpty(phoneList))
                    {
                        phoneList = appPhoneList;
                    }
                    else
                    {
                        phoneList.retainAll(appPhoneList);
                    }
                }
            }
        }

        return phoneList;
    }

    @Override
    public void sendMessageTaskToProxy(SendMessage sendMessage) {
        Integer sendCount = (Integer) RedisUtil.getObject("sendMessageTask_" + sendMessage.getId());
        if (sendCount == null)
        {
            sendCount = 1;
        }
        else
        {
            sendCount = sendCount + 1;
        }
        // 最多只能发送10次，若10次都没发送成功，则默认失败
        if (sendCount > 10)
        {
            // 执行失败
            sendMessage.setTaskStatus(2);
            sendMessageTaskService.update(sendMessage);
            RedisUtil.delkeyObject("sendMessageTask_" + sendMessage.getId());
        }
        else
        {
            RedisUtil.setObject("sendMessageTask_" + sendMessage.getId(), sendCount);
            sendMessage(sendMessage);
        }


    }


    private boolean sendMessage(SendMessage sendMessage)
    {
        boolean isSend = true;
        if (sendMessage != null && StringUtils.isNotEmpty(sendMessage.getSenderPhone()))
        {
            // 根据指定的手机号获取在线的设备
            DpDto dp = deviceMapper.getDeviceAndProxyByPhone(sendMessage.getSenderPhone());
            if (dp != null)
            {
                SendRequest sendRequest = new SendRequest();
                sendRequest.setDeviceSn(dp.getDeviceSn());
                sendRequest.setPhoneNumber(sendMessage.getReceiverPhone());
                sendRequest.setSmsContent(sendMessage.getText());
                CmdRoot cmdRoot = OperateCmdFactory.getInstance().sendMessage(sendRequest);

                String jsonStr = JSON.toJSONString(cmdRoot);
                String proxyIpMac = dp.getProxyIp() + "_" + dp.getProxyMac();

                Channel channel = NettyChannelMap.getChannel(proxyIpMac);
                if (channel != null)
                {
                    SyncFuture<SendResponse> syncFuture = new SyncFuture<>();
                    NettyServerHandler.syncCountDownWatch.put(cmdRoot.getSEQ_NO(), syncFuture);

                    NettyServerHandler.sendMsgToProxy(channel, proxyIpMac, jsonStr);
                    LOGGER.info("已经向手机(" + dp.getTelNum() + ")设备下发了短信发送指令");
                    try
                    {
                        SendResponse sendResponse = syncFuture.get(5, TimeUnit.SECONDS);
                        if (sendResponse != null)
                        {
                            if (0 == sendResponse.getResult())
                            {
                                // 短信发送成功，无需再次发送
                                sendMessage.setTaskStatus(1);
                                sendMessageTaskService.update(sendMessage);
                            }
                            else if (1 == sendResponse.getResult())
                            {
                                sendMessage.setTaskStatus(2);
                                sendMessageTaskService.update(sendMessage);
                            }
                            else if (2 == sendResponse.getResult())
                            {
                                sendMessage.setTaskStatus(3);
                                sendMessageTaskService.update(sendMessage);
                            }
                            // 已收到消息，无需再次发送
                            isSend = false;
                        }
                    }
                    catch (Exception e)
                    {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        }
        return isSend;
    }
}
