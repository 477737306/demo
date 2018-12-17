package com.cmit.testing.listener.proxy.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmit.testing.entity.app.Device;
import com.cmit.testing.entity.app.MsgHead;
import com.cmit.testing.entity.proxy.ImageTransParam;
import com.cmit.testing.listener.msg.ApkMessage;
import com.cmit.testing.listener.msg.DeviceMessage;
import com.cmit.testing.listener.operatecmd.OperateCmdFactory;
import com.cmit.testing.listener.proxy.netty.NettyChannelMap;
import com.cmit.testing.listener.proxy.netty.NettyServerHandler;
import com.cmit.testing.listener.proxy.netty.UdpServerHandler;
import com.cmit.testing.listener.websocket.DeviceListWebSocketServer;
import com.cmit.testing.service.app.AppCaseService;
import com.cmit.testing.service.app.DeviceService;
import com.cmit.testing.service.app.TaskService;
import com.cmit.testing.utils.Constants;
import com.cmit.testing.utils.SpringContextHolder;
import com.cmit.testing.utils.StringUtils;
import com.cmit.testing.utils.app.DeviceConstant;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author XieZuLiang
 * @description  远程真机webSocket，向前端传输截图base64数据
 * @date 2018/9/4 0004 下午 8:22.
 */
@ServerEndpoint(value = "/api/v1/webSocket/realDevice.spi")
@Component
public class DeviceWebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceWebSocketServer.class);

    /**
     * 存放所有webSocket连接对象
     */
    private static final ConcurrentMap<String, Set<DeviceWebSocketServer>> connectionMap = new ConcurrentHashMap<String, Set<DeviceWebSocketServer>>();

    /**
     * 临时图片存储
     */
    private static final ConcurrentHashMap<String,String> imageMap = new ConcurrentHashMap<>();

    /**
     * 当前连接session
     */
    private Session session;
    /**
     * 当前连接的终端手机唯一序列号
     */
    private String deviceSn;

    /**
     * 终端Proxy的IP地址
     */
    private String proxyIp;

    /**
     * 终端Proxy的Mac地址
     */
    private String proxyMac;

    /**
     * 当前连接状态是选用或选看（show-选看；ctrl-选用）
     */
    private String useStatus;

    public DeviceWebSocketServer() {
    }

    /**
     * 连接时触发
     * @param session
     */
    @OnOpen
    public void onStart(Session session)
    {
        LOGGER.info("接收到Web页面socket连接：" + session.getId());
        this.session = session;
    }

    /**
     * 连接关闭时触发
     */
    @OnClose
    public void onEnd()
    {
        LOGGER.info("Web页面socket连接关闭：deviceSn="+ deviceSn + "; sessionId=" + session.getId() + "; session=" + session.isOpen());

        //删除webSocket连接对象
        if (connectionMap.containsKey(deviceSn))
        {
            connectionMap.get(deviceSn).remove(this);
            if (connectionMap.get(deviceSn).size() == 0)
            {
                connectionMap.remove(deviceSn);
                imageMap.remove(deviceSn);
            }
        }
        try
        {
            this.session.close();
        }
        catch (IOException e)
        {
            LOGGER.info("webSocket关闭异常：sessionId=" + session.getId(), e);
        }

        // 删除并关闭proxy的图片传输连接(所有页面连接关闭时触发)
        if (!connectionMap.containsKey(deviceSn))
        {
            // 给proxy发送断开连接请求
            if (proxyIp != null && NettyChannelMap.getChannel(proxyIp + "_" + proxyMac) != null)
            {
                MsgHead<ImageTransParam> request = OperateCmdFactory.getInstance().stopImgTransRequest(deviceSn);
                String jsonStr = JSON.toJSONString(request);
                // 发送消息
                String proxyIpMac = proxyIp + "_" + proxyMac;
                NettyServerHandler.sendMsgToProxy(NettyChannelMap.getChannel(proxyIpMac), proxyIpMac, jsonStr);

                LOGGER.info("断开图片连接请求发送成功！");
            }
        }

        UdpServerHandler.getTempRepository().invalidate(deviceSn);

        if ("ctrl".equals(useStatus))
        {
            // 选用连接关闭时，修改真机状态。
            TaskService taskService = SpringContextHolder.getBean("taskService");
            taskService.schedulingTask(deviceSn);
        }

    }

    /**
     * 接收到消息时触发
     * @param msg
     */
    @OnMessage
    public void onMessage(String msg)
    {
        LOGGER.info("收到Web发送的消息: " + msg + "; 当前sessionId=" + session.getId());

        if (msg.contains("_"))
        {
            String[] msgArray = msg.split("_");
            this.deviceSn = msgArray[0];
            this.proxyIp = msgArray[1];
            this.proxyMac = msgArray[2];
            this.useStatus = msgArray[3];

            if (connectionMap.containsKey(deviceSn))
            {
                connectionMap.get(deviceSn).add(this);

                try
                {
                    this.session.getBasicRemote().sendText(imageMap.get(deviceSn));
                }
                catch (IOException e)
                {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            else
            {
                Set<DeviceWebSocketServer> webSocketConnectionSet = new CopyOnWriteArraySet<DeviceWebSocketServer>();
                webSocketConnectionSet.add(this);
                connectionMap.put(deviceSn, webSocketConnectionSet);
            }

            LOGGER.info(" connectionMap.get(deviceSn) = " + connectionMap.get(deviceSn));
        }
        else
        {
            List<String> deviceSnList = Arrays.asList(msg.split(";"));
            if (CollectionUtils.isNotEmpty(deviceSnList))
            {
                for (String deviceSn : deviceSnList)
                {
                    if (connectionMap.containsKey(deviceSn))
                    {
                        connectionMap.get(deviceSn).add(this);
                    }
                    else
                    {
                        Set<DeviceWebSocketServer> webSocketConnectionSet = new CopyOnWriteArraySet<DeviceWebSocketServer>();
                        webSocketConnectionSet.add(this);
                        connectionMap.put(deviceSn, webSocketConnectionSet);
                    }

                    LOGGER.info(" connectionMap.get(deviceSn) = " + connectionMap.get(deviceSn));
                }
            }

        }
    }

    /**
     * 异常时触发
     */
    @OnError
    public void onError(Throwable t)
    {
        LOGGER.error("Error: " + t.toString(), t);
    }


    /**
     * 向页面推送消息
     * @param deviceSn
     * @param msg
     */
    public static void sendMsgToPage(String deviceSn, String msg)
    {
        if (connectionMap.containsKey(deviceSn))
        {
            Set<DeviceWebSocketServer> connObjSet = connectionMap.get(deviceSn);
            if (connObjSet == null)
            {
                LOGGER.info("webSocket连接对象为空");
            }

            if (StringUtils.isNotEmpty(msg) && msg.startsWith("data:image"))
            {
                imageMap.put(deviceSn, msg);
            }

            for (DeviceWebSocketServer client : connObjSet)
            {
                try
                {
                    synchronized (client)
                    {
                        client.session.getBasicRemote().sendText(msg);
                    }
                }
                catch (IOException e)
                {
                    LOGGER.debug("Error: Failed to send message to client", e);

                    connObjSet.remove(client);

                    if (connectionMap.get(deviceSn).size() == 0)
                    {
                        connectionMap.remove(deviceSn);
                    }
                    try
                    {
                        client.session.close();
                    } catch (IOException e1) {
                        // Ignore
                    }
                }
            }
        }
        else
        {
            LOGGER.info("webSocket连接队列中无 deviceSn = " + deviceSn + " 的连接");
        }
    }

    /**
     * 向页面推送应用相关消息
     * @param apkMsg
     */
    public static void sendApkMsgToPage(ApkMessage apkMsg)
    {
        if (connectionMap.containsKey(apkMsg.getDeviceSn()))
        {
            Set<DeviceWebSocketServer> connObjSet = connectionMap.get(apkMsg.getDeviceSn());
            if (connObjSet == null)
            {
                LOGGER.info("webSocket连接对象为空");
            }
            for (DeviceWebSocketServer client : connObjSet)
            {
                try
                {
                    synchronized (client)
                    {
                        client.session.getBasicRemote().sendText(JSONObject.toJSONString(apkMsg));
                    }
                }
                catch (IOException e)
                {
                    LOGGER.debug("Error: Failed to send message to client", e);
                    connObjSet.remove(client);

                    if (connectionMap.get(apkMsg.getDeviceSn()).size() == 0)
                    {
                        connectionMap.remove(apkMsg.getDeviceSn());
                    }
                    try
                    {
                        client.session.close();
                    }
                    catch (IOException e1)
                    {
                        // Ignore
                    }
                }
            }
        }
        else
        {
            LOGGER.info("webSocket连接队列中无 deviceSn = " + apkMsg.getDeviceSn() + " 的连接");
        }
    }

    public static ConcurrentMap<String, Set<DeviceWebSocketServer>> getConnectionMap() {
        return connectionMap;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }
}
