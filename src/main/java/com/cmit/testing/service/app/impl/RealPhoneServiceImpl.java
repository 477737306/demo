package com.cmit.testing.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.cmit.testing.dao.app.DeviceMapper;
import com.cmit.testing.entity.app.Device;
import com.cmit.testing.entity.app.MsgHead;
import com.cmit.testing.entity.proxy.CmdBody;
import com.cmit.testing.entity.proxy.CmdRoot;
import com.cmit.testing.entity.proxy.ImageTransParam;
import com.cmit.testing.exception.TestSystemException;
import com.cmit.testing.listener.msg.DeviceMessage;
import com.cmit.testing.listener.operatecmd.CmdOperationBO;
import com.cmit.testing.listener.operatecmd.OperateCmdFactory;
import com.cmit.testing.listener.proxy.netty.ImageChannelMap;
import com.cmit.testing.listener.proxy.netty.NettyChannelMap;
import com.cmit.testing.listener.proxy.netty.NettyServerHandler;
import com.cmit.testing.listener.proxy.netty.NettyServerUDP;
import com.cmit.testing.listener.proxy.websocket.DeviceWebSocketServer;
import com.cmit.testing.listener.websocket.DeviceListWebSocketServer;
import com.cmit.testing.service.app.RealPhoneService;
import com.cmit.testing.utils.app.ConstantUtil;
import com.cmit.testing.utils.app.DeviceConstant;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/9/13.
 */
@Service("realPhoneService")
public class RealPhoneServiceImpl implements RealPhoneService{

    @Autowired
    private DeviceMapper deviceMapper;

    private final static Logger LOGGER = LoggerFactory.getLogger(RealPhoneServiceImpl.class);

    /**
     * 发送远程操控指令
     * @param cbo
     */
    @Override
    public void sendCmd(CmdOperationBO cbo)
    {
        if (!DeviceWebSocketServer.getConnectionMap().containsKey(cbo.getDeviceSn()))
        {
            throw new TestSystemException("设备未连接上页面");
        }
        CmdRoot cmdRoot = buildCmdRoot(cbo);
        if (cmdRoot == null)
        {
            throw new TestSystemException("操作指令有误");
        }
        String jsonStr = JSON.toJSONString(cmdRoot);
        Channel channel = NettyChannelMap.getChannel(cbo.getProxyIpMac());
        if (channel == null)
        {
            LOGGER.info("deviceSn:" + cbo.getDeviceSn() + "对应proxy连接异常！");
            throw new TestSystemException("设备连接异常");
        }
        NettyServerHandler.sendMsgToProxy(channel, cbo.getProxyIpMac(), jsonStr);
        LOGGER.info("操作指令发送成功");
    }

    /**
     * 发起手机选用请求
     */
    @Override
    public void showRealPhone(String deviceSn, String proxyIp, String proxyMac)
    {
        if (StringUtils.isEmpty(proxyIp))
        {
            throw new TestSystemException("设备未连接");
        }
        Device example = new Device();
        example.setDeviceSn(deviceSn);
        List<Device> list = deviceMapper.getList(example);
        if (CollectionUtils.isEmpty(list))
        {
            throw new TestSystemException("未找到对应手机设备");
        }
        if (DeviceConstant.ONLINE_STATUS_0.equals(list.get(0).getOnlineStatus()))
        {
            throw new TestSystemException("手机设备已离线");
        }
        String portStr = ConstantUtil.SERVER_PORT == null ? "8899" : ConstantUtil.SERVER_PORT;
        Integer port = Integer.parseInt(portStr);

        NettyServerUDP serverBootstrap = ImageChannelMap.getServerBootstrap(portStr);
        if (serverBootstrap == null)
        {
            //server与proxy之间的通信
            NettyServerUDP bootstrap = new NettyServerUDP(port);
            ImageChannelMap.addServerBootstrap(portStr, bootstrap);
        }
        // 当已经有链接时不再重发发送连接请求
        SocketChannel channel = ImageChannelMap.getChannelMap().get(deviceSn);
        if (channel == null || !channel.isActive())
        {
            LOGGER.info("发送图片传输连接请求！");
            ImageChannelMap.getChannelMap().remove(deviceSn);
            MsgHead<ImageTransParam> request = OperateCmdFactory.getInstance().startImgTransRequest(deviceSn,port,1);
            String jsonStr = JSON.toJSONString(request);
            Channel proxyChannel = NettyChannelMap.getChannel(proxyIp+"_"+proxyMac);
            if (proxyChannel != null)
            {
                NettyServerHandler.sendMsgToProxy(proxyChannel, proxyIp + "_" + proxyMac, jsonStr);// 发送消息

                LOGGER.info("发送图片传输连接请求成功!");

                // 修改手机使用状态
                Device record = new Device();
                record.setDeviceSn(deviceSn);
                record.setUseStatus(DeviceConstant.USER_STATUS_1);
                deviceMapper.updateDeviceByDeviceSn(record);

                DeviceMessage deviceMessage = new DeviceMessage();
                deviceMessage.setType("occupy");
                deviceMessage.setDeviceSn(deviceSn);
                deviceMessage.setUseStatus("1");
                DeviceListWebSocketServer.sendMsgToPage(deviceMessage);
            }
            else
            {
                LOGGER.info("deviceSn:" + deviceSn + "对应proxy连接异常！");
                throw new TestSystemException("该设备连接异常！");
            }
        }
        else
        {
            LOGGER.info("存在图片传输连接，无需重新发送图片连接请求！");
            // 修改手机使用状态
            Device record = new Device();
            record.setDeviceSn(deviceSn);
            record.setUseStatus(DeviceConstant.USER_STATUS_1);
            deviceMapper.updateDeviceByDeviceSn(record);
        }
    }

    /**
     * 构建请求操控指令对象
     * @param cbo
     * @return
     */
    private CmdRoot buildCmdRoot(CmdOperationBO cbo)
    {
        CmdRoot cmdRoot = null;
        switch (cbo.getCmdType())
        {
            // 点击
            case DeviceConstant.CMD_CLICK:
                cmdRoot = OperateCmdFactory.getInstance().click(cbo.getDeviceSn(), cbo.getStartX(), cbo.getStartY());
                break;
            // 长按
            case DeviceConstant.CMD_LONG_CLICK:
                cmdRoot = OperateCmdFactory.getInstance().longClick(cbo.getDeviceSn(), cbo.getStartX(), cbo.getStartY(), cbo.getDuration());
                break;
            // 滑动
            case DeviceConstant.CMD_SWIPE:
                cmdRoot = OperateCmdFactory.getInstance().swipe(cbo.getDeviceSn(), cbo.getStartX(), cbo.getStartY(), cbo.getEndX(), cbo.getEndY(), cbo.getDuration());
                break;
            // 点击Home键
            case DeviceConstant.CMD_HOME:
                cmdRoot = OperateCmdFactory.getInstance().home(cbo.getDeviceSn());
                break;
            // 点击菜单键
            case DeviceConstant.CMD_MENU:
                cmdRoot = OperateCmdFactory.getInstance().menu(cbo.getDeviceSn());
                break;
            // 点击返回键
            case DeviceConstant.CMD_BACK:
                cmdRoot = OperateCmdFactory.getInstance().back(cbo.getDeviceSn());
                break;
            // 点击搜索按钮
            case DeviceConstant.CMD_SEARCH:
                cmdRoot = OperateCmdFactory.getInstance().search(cbo.getDeviceSn());
                break;
            // 唤醒/锁屏
            case DeviceConstant.CMD_POWER:
                cmdRoot = OperateCmdFactory.getInstance().power(cbo.getDeviceSn());
                break;
            // 截屏
            case DeviceConstant.CMD_SCREENCAP:
                cmdRoot = OperateCmdFactory.getInstance().screencap(cbo.getDeviceSn());
                break;
            // 重启
            case DeviceConstant.CMD_REBOOT:
                cmdRoot = OperateCmdFactory.getInstance().reboot(cbo.getDeviceSn());
                break;
            // 输入文本
            case DeviceConstant.CMD_INPUT:
                cmdRoot = OperateCmdFactory.getInstance().input(cbo.getDeviceSn(), cbo.getInputText());
                break;
            // 发送短信
            case DeviceConstant.CMD_TYPE_SEND_SMS:
                cmdRoot = OperateCmdFactory.getInstance().sendSms(cbo.getDeviceSn(), cbo.getTelNum(), cbo.getSmsContent());
                break;
            // 打开指定网址
            case DeviceConstant.CMD_OPEN_URL:
                String openUrl = cbo.getOpenUrl();
                if (StringUtils.isEmpty(openUrl))
                {
                    throw new TestSystemException("请输入要打开的网址");
                }
                if (!openUrl.startsWith("http"))
                {
                    openUrl = "http://" + openUrl;
                }
                cmdRoot = OperateCmdFactory.getInstance().openUrl(cbo.getDeviceSn(), openUrl);
                break;
            // 回车键
            case DeviceConstant.CMD_ENTER:
                cmdRoot = OperateCmdFactory.getInstance().enter(cbo.getDeviceSn());
                break;
            // 退格键
            case DeviceConstant.CMD_BACKSPACE:
                cmdRoot = OperateCmdFactory.getInstance().backspace(cbo.getDeviceSn());
                break;
            // 安装APK
            case DeviceConstant.CMD_INSTALL_APK:
                cmdRoot = OperateCmdFactory.getInstance().installApk(cbo.getDeviceSn(), cbo.getApkUrl(), cbo.getPkgName(), cbo.getApkVersion());
                break;
            // 卸载APK
            case DeviceConstant.CMD_UNINSTALL_APK:
                cmdRoot = OperateCmdFactory.getInstance().uninstallApk(cbo.getDeviceSn(), cbo.getPkgName());
                break;
            default:
                LOGGER.error("远程操控手机设备的指令匹配错误：CmdType=" + cbo.getCmdType());
                break;
        }
        return cmdRoot;
    }
}
