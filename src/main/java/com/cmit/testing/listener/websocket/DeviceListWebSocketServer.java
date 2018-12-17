package com.cmit.testing.listener.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.listener.msg.DeviceMessage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 更新设备列表中的设备状态
 */
@ServerEndpoint(value = "/api/v1/webSocket/deviceList.spi")
@Component
public class DeviceListWebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceListWebSocketServer.class);

    /**
     * 存放所有websocket连接对象
     */
    private static final ConcurrentMap<String, Set<DeviceListWebSocketServer>> connectionMap = new ConcurrentHashMap<String, Set<DeviceListWebSocketServer>>();

    private String deviceSn;

    private String proxyIp;

    private String proxyMac;
    /**
     * 当前连接是选用还是选看 （选看-show；选用-ctrl）
     */
    private String useStatus;
    /**
     * 当前连接session
     */
    private Session session;

    private static final List<Session> sessionList = new ArrayList<>();

    @OnOpen
    public void onStart(final Session session)
    {
        LOGGER.info("接收到Web页面socket连接：" + session.getId());
        this.session = session;

        sessionList.add(session);
    }

    @OnClose
    public void onEnd()
    {
        LOGGER.info("Web页面socket连接关闭：deviceSn="+ deviceSn + "; sessionId=" + session.getId() + "; session=" + session.isOpen());

        try
        {
            this.session.close();
        }
        catch (IOException e)
        {
            LOGGER.info("webSocket关闭异常：sessionId=" + session.getId(), e);
        }

        sessionList.remove(session);
        LOGGER.info("sessionList size: " + sessionList.size()+"");
    }

    @OnMessage
    public void onMessage(String msg) {
        LOGGER.info("接收到Web页面的消息：----------> " + msg);
    }

    @OnError
    public void onError(Throwable t)
    {
        LOGGER.error("Error: " + t.toString(), t);
    }


    /**
     * 向所有在线用户发送消息
     * @param msg
     */
    public static void sendMsgToPage(String msg)
    {
        for (Session session : sessionList)
        {
            try
            {
                session.getBasicRemote().sendText(msg);
            }
            catch (IOException e)
            {
                LOGGER.error("发送消息失败:"+e.getMessage(), e);
            }
        }
    }


    public static void sendMsgToPage(DeviceMessage deviceMessage)
    {
        List<DeviceMessage> msgList = new ArrayList<>();
        msgList.add(deviceMessage);
        String jsonStr = JSONObject.toJSONString(msgList);
        for (Session session : sessionList)
        {
            try
            {
                session.getBasicRemote().sendText(jsonStr);
            }
            catch (IOException e)
            {
                LOGGER.error("发送消息失败:"+e.getMessage(), e);
            }
        }
    }

    public static void sendMsgListToPage(List<DeviceMessage> msgList)
    {
        if (CollectionUtils.isNotEmpty(msgList))
        {
            String jsonStr = JSON.toJSONString(msgList);

            for (Session session : sessionList)
            {
                try
                {
                    session.getBasicRemote().sendText(jsonStr);
                }
                catch (IOException e)
                {
                    LOGGER.error("发送消息失败:"+e.getMessage(), e);
                }
            }
        }
    }

    public static ConcurrentMap<String, Set<DeviceListWebSocketServer>> getConnectionMap() {
        return connectionMap;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }
}
