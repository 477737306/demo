package com.cmit.testing.listener.proxy.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cmit.testing.common.syncmsg.SyncFuture;
import com.cmit.testing.entity.Message;
import com.cmit.testing.entity.SimCard;
import com.cmit.testing.entity.app.*;
import com.cmit.testing.entity.proxy.*;
import com.cmit.testing.listener.msg.ApkMessage;
import com.cmit.testing.listener.operatecmd.OperateCmdFactory;
import com.cmit.testing.listener.proxy.TaskProcessResultHandler;
import com.cmit.testing.listener.proxy.websocket.DeviceWebSocketServer;
import com.cmit.testing.listener.response.AppOperationResponse;
import com.cmit.testing.listener.response.SendResponse;
import com.cmit.testing.service.SimCardService;
import com.cmit.testing.service.app.*;
import com.cmit.testing.utils.AppConstants;
import com.cmit.testing.utils.Constants;
import com.cmit.testing.utils.JsonUtil;
import com.cmit.testing.utils.SpringContextHolder;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ResourceUtils;

import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XieZuLiang
 * @description 处理Proxy与Server之间的通信
 * @date 2018/8/30 0030 下午 5:34.
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);

    public static  final ConcurrentHashMap<String, SyncFuture> syncCountDownWatch = new ConcurrentHashMap<>();

    private final String imageBase64Head = "data:image/jpg;base64,";
    /**
     * 分隔符
     */
    private static String split;

    /**
     * 端口
     */
    private int serverPort;
    /**
     * 图像传输连接端口
     */
    private static final Integer PORT = 8899;

    public NettyServerHandler(int serverPort, String split){
        this.serverPort = serverPort;
        NettyServerHandler.split = split;
    }

    /**
     * 断开连接时调用
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        String proxyIpMac = "";
        Map<String, SocketChannel> map = NettyChannelMap.getChannelMap();
        for (Map.Entry entry : map.entrySet()) {
            if (ctx.channel().equals(entry.getValue())) {
                proxyIpMac = (String) entry.getKey();
            }
        }
        NettyChannelMap.removeChannel((SocketChannel) ctx.channel());
        ctx.channel().close();
        ctx.close();
        LOGGER.error("Proxy "+ proxyIpMac + " 断开连接");

        if (StringUtils.isNotBlank(proxyIpMac))
        {
            String []proxy = proxyIpMac.split("_");

            // 修改设备状态及SIM卡状态
            DeviceService deviceService = SpringContextHolder.getBean("deviceService");
            Map<String, Object> offLineMap = new HashMap<>();
            offLineMap.put("onlineStatus", Constants.OFFLINE_STATUS);
            offLineMap.put("proxyIp", proxy[0]);
            offLineMap.put("proxyMac", proxy[1]);
            deviceService.updateByProxy(offLineMap);

            // 修改Proxy状态
            AppProxyService appProxyService = SpringContextHolder.getBean("appProxyService");
            AppProxy ap = new AppProxy();
            ap.setProxyIp(proxy[0]);
            ap.setProxyMac(proxy[1]);
            ap.setOnlineStatus(AppConstants.PROXY_ONLINE_STATUS_1);
            appProxyService.updateByProxyIpAndMac(ap);
        }

    }

    /**
     * 当Netty由于IO错误或者处理器在处理事件时抛出异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        LOGGER.error("连接通道异常信息：" + cause.getStackTrace());
        ctx.channel().close();
        ctx.close();
    }

    /**
     * 关闭连接
     * @param channel
     */
    public static void closeChannel(Channel channel){
        if (channel != null)
        {
            channel.close();
        }
    }

    /**
     * 有客户端建立连接时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress inetSocketAddress = null;
        Channel channel = ctx.channel();
        if (channel != null) {
            inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        }
        LOGGER.info("Proxy " + inetSocketAddress + " 已经与 Server " + serverPort + " 端口建立连接！");
    }

    /**
     * 向Proxy发送消息
     *
     * @param channel
     * @param proxy
     * @param msg
     */
    public static void sendMsgToProxy(Channel channel, String proxy, String msg)
    {
        try {
            if(channel!=null){
                channel.writeAndFlush((msg + split).getBytes("UTF-8"));
                channel.flush();
                LOGGER.info("向proxy " + proxy + " 发送消息成功。" + msg);
            }else{
                LOGGER.error("proxy连接的channel为null！");
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("给proxy " + proxy + " 发送消息失败！", e);
            e.printStackTrace();
        }
    }

    /**
     * 接收到客户端的消息时调用
     *
     * @param ctx
     * @param baseMsg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] baseMsg) throws Exception {
        String baseMsgStr = new String(baseMsg, "UTF-8");
        if (StringUtils.isNotBlank(baseMsgStr) && baseMsgStr.indexOf("{") != -1) {
            baseMsgStr = baseMsgStr.substring(baseMsgStr.indexOf("{"), baseMsgStr.length());
        }
        if (baseMsgStr.startsWith("{")) {
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_HEART_BEAT + "\"")) {
                LOGGER.debug("收到Proxy HeartBeat数据：" + baseMsgStr);
            } else {
                LOGGER.info("收到Proxy数据：" + baseMsgStr);
            }
        }

        //获取Proxy的Ip地址
        String proxyIp = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();

        // 处理消息
        try {

            // 手机重启操作
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_REBOOT + "\""))
            {
                phoneDeviceReboot(ctx, baseMsgStr);
            }
            // Proxy注册消息
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_PROXY_REGISTER + "\""))
            {
                proxyRegister(ctx, baseMsgStr, proxyIp);
            }
            // 设备自动注册消息
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_DEVICE_REGISTER + "\""))
            {
                deviceRegister(ctx, baseMsgStr);
            }
            // 心跳消息
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_HEART_BEAT + "\""))
            {
                beartHeat(ctx, baseMsgStr);
            }
            // 下发用例响应消息
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_STARTTASK + "\""))
            {
                startTaskResponse(ctx, baseMsgStr);
            }
            // 回传测试结果
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_TEST_RESULT + "\""))
            {
                receviceTestResult(baseMsgStr);
            }
            // 获取用例当前执行的信息
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_CURRENT_TASK + "\""))
            {
                changeTask(ctx, baseMsgStr);
            }
            // 获取脚本当前执行到第几步
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_CURRENT_STEP + "\""))
            {
                changeStep(ctx, baseMsgStr);
            }
            // 接收短信内容 并校验
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_SMS_VERIFY + "\""))
            {
                verifySmsContent(baseMsgStr);
            }
            // 发送短信时Proxy返回的消息
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_TYPE_SEND_MSG + "\""))
            {
                MsgHead<SendResponse> msgHead = JSON.parseObject(baseMsgStr, new TypeReference<MsgHead<SendResponse>>(){});

                if (syncCountDownWatch.containsKey(msgHead.getSEQ_NO()))
                {
                    syncCountDownWatch.get(msgHead.getSEQ_NO()).setResponse(msgHead.getCMD_PARAM());
                    syncCountDownWatch.remove(msgHead.getSEQ_NO());
                }
            }
            // 安装APK应用
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_INSTALL_APK + "\""))
            {
                installApk(ctx, baseMsgStr);
            }
            // 卸载APK应用
            if (baseMsgStr.contains("\"CMD_TYPE\":\"" + AppConstants.CMD_UNINSTALL_APK + "\""))
            {
                uninstallApk(ctx, baseMsgStr);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
        } finally {
            ReferenceCountUtil.release(baseMsg);
        }

    }

    /**
     * 手机设备重启后处理
     */
    public void phoneDeviceReboot(ChannelHandlerContext ctx, String baseMsgStr)
    {
        JSONObject json = JSONObject.parseObject(baseMsgStr);
        String msg = json.getJSONObject("CMD_PARAM").getString("msg");
        String deviceSn = json.getJSONObject("CMD_PARAM").getString("deviceSn");
        if ("OK".equals(msg))
        {
            InputStream is = null;
            File imageFile = null;
            try
            {
                is = NettyServerHandler.class.getResourceAsStream("/config/black.jpg");
                imageFile = new File("black.jpg");
                FileUtils.copyInputStreamToFile(is, imageFile);

                LOGGER.info(" -----> normal Reboot ");
                ImageChannelMap.removeChannel((SocketChannel) ctx.channel());

                byte[] imageByte = imageToByte(imageFile);
                Base64Encoder encoder = new Base64Encoder();
                String imgBase64 = imageBase64Head + encoder.encode(imageByte);
                DeviceWebSocketServer.sendMsgToPage(deviceSn, imgBase64);

            } catch (IOException e) {
                String imagePath = "/home/atap/black.jpg";
                imageFile = new File(imagePath);

                LOGGER.info(" -----> Reboot ");
                ImageChannelMap.removeChannel((SocketChannel) ctx.channel());

                byte[] imageByte = imageToByte(imageFile);
                Base64Encoder encoder = new Base64Encoder();
                String imgBase64 = imageBase64Head + encoder.encode(imageByte);
                DeviceWebSocketServer.sendMsgToPage(deviceSn, imgBase64);
            }finally {
                FileUtils.deleteQuietly(imageFile);
                IOUtils.closeQuietly(is);
            }
        }
    }

    /**
     * 图片转字节数组
     */
    private byte[] imageToByte(File file)
    {
        byte[] data = null;
        FileImageInputStream input = null;
        ByteArrayOutputStream output = null;
        try
        {
            input = new FileImageInputStream(file);
            output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1)
            {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
        }
        catch (Exception ex1)
        {
            ex1.printStackTrace();
        }
        finally
        {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }
        return data;
    }


    /**
     * Proxy注册
     */
    public void proxyRegister(ChannelHandlerContext ctx, String baseMsgStr, String proxyIp){
        MsgHead<ProxyRegisterResponse> msg = new MsgHead<ProxyRegisterResponse>(AppConstants.CMD_TYPE_PROXY_REGISTER_RESPONSE,AppConstants.CMD_TYPE_PROXY_REGISTER);
        ProxyRegisterResponse response = new ProxyRegisterResponse();
        msg.setCMD_PARAM(response);

        try {

            MsgHead<ProxyRegisterRequest> proxyRegister = JSON.parseObject(baseMsgStr, new TypeReference<MsgHead<ProxyRegisterRequest>>(){});
            msg.setSEQ_NO(proxyRegister.getSEQ_NO());
            String macAddress = proxyRegister.getCMD_PARAM().getMacAddress();

            // 将连接channel放入map对象中
            NettyChannelMap.addChannel(proxyIp + "_" + macAddress, (SocketChannel) ctx.channel());
            response.setResultCode(AppConstants.CMD_TYPE_PROXY_REGISTER_1);

            // 存储proxy信息
            AppProxy ap = new AppProxy();
            ap.setProxyIp(proxyIp);
            ap.setProxyMac(macAddress);
            AppProxyService appProxyService = SpringContextHolder.getBean("appProxyService");

            List<AppProxy> apList = appProxyService.getList(ap);
            if (apList.size() == 0) {
                ap.setOnlineStatus(AppConstants.PROXY_ONLINE_STATUS_0);
                appProxyService.insertSelective(ap);
            } else {
                ap.setProxyId(apList.get(0).getProxyId());
                ap.setOnlineStatus(AppConstants.PROXY_ONLINE_STATUS_0);
                appProxyService.updateByPrimaryKeySelective(ap);
            }
            LOGGER.info("Proxy: " + proxyIp + "_" + macAddress + " 注册成功！");

            sendMsgToProxy(ctx.channel(), proxyIp + "_" + macAddress, JSON.toJSONString(msg));

        }catch (Exception e){
            LOGGER.info("Proxy: " + proxyIp + " 注册失败！", e);
            response.setResultCode(AppConstants.CMD_TYPE_PROXY_REGISTER_2);
            sendMsgToProxy(ctx.channel(), proxyIp, JSON.toJSONString(msg));
        }
    }

    /**
     * 设备自动注册
     */
    public void deviceRegister(ChannelHandlerContext ctx, String baseMsgStr) throws Exception{
        String porxyIpMac = "";
        Map<String, SocketChannel> map = NettyChannelMap.getChannelMap();
        for (Map.Entry entry : map.entrySet()) {
            if (ctx.channel().equals(entry.getValue())) {
                porxyIpMac = (String) entry.getKey();
            }
        }

        if (StringUtils.isNotBlank(porxyIpMac))
        {
            // 真机选用重启的情况会发自动注册请求。收到后判断页面是否有webSocket连接，有则再发送图片传输连接请求。
            MsgHead<DeviceInfo> deviceRegister = JSON.parseObject(baseMsgStr, new TypeReference<MsgHead<DeviceInfo>>() {});

            // webSocket连接Map中有此deviceSn
            if (DeviceWebSocketServer.getConnectionMap().containsKey(deviceRegister.getCMD_PARAM().getDeviceSn())) {
                // 图像传输ChannelMap中无此client连接
                if (!ImageChannelMap.getChannelMap().containsKey(deviceRegister.getCMD_PARAM().getDeviceSn())) {
                    // 发送新的图像连接请求
                    MsgHead<ImageTransParam> request = OperateCmdFactory.getInstance().startImgTransRequest(
                            deviceRegister.getCMD_PARAM().getDeviceSn(), PORT, 1);// 选用
                    String jsonStr = JSON.toJSONString(request);
                    sendMsgToProxy(ctx.channel(), porxyIpMac, jsonStr);
                    LOGGER.info("发送真机选用重连请求 deviceSN=" + deviceRegister.getCMD_PARAM().getDeviceSn());
                }
            }
            // 手机自动注册
            DeviceService deviceService = SpringContextHolder.getBean("deviceService");
            deviceService.deviceRegister(deviceRegister.getCMD_PARAM(), porxyIpMac);
        }

    }

    /**
     * 心跳
     */
    public void beartHeat(ChannelHandlerContext ctx, String baseMsgStr) throws Exception{
        String porxyIpMac = getProxyIpMac(ctx);

        LOGGER.info("BeartHeat: " + porxyIpMac);
        if (StringUtils.isNotEmpty(porxyIpMac))
        {
            MsgHead<BeartHeatInfo> beartHeat = JSON.parseObject(baseMsgStr, new TypeReference<MsgHead<BeartHeatInfo>>() {
            });
            DeviceService deviceService = SpringContextHolder.getBean("deviceService");
            deviceService.beartHeat(beartHeat.getCMD_PARAM(), porxyIpMac);
        }
        else
        {
            LOGGER.error("ProxyIpMac为空，连接通道列表中无对应的Proxy。");
        }
    }


    /**
     * 获取当前响应消息的proxy
     */
    private String getProxyIpMac(ChannelHandlerContext ctx){
        String porxyIpMac = "";
        Map<String, SocketChannel> map = NettyChannelMap.getChannelMap();
        for (Map.Entry entry : map.entrySet())
        {
            if (ctx.channel().equals(entry.getValue()))
            {
                porxyIpMac = (String) entry.getKey();
            }
        }
        return porxyIpMac;
    }

    /**
     * 下发用例响应信息
     */
    public void startTaskResponse(ChannelHandlerContext ctx, String baseMsgStr) {
        String proxyIpMac = getProxyIpMac(ctx);
        if (StringUtils.isNotBlank(proxyIpMac))
        {
            RootJson root = JsonUtil.getBean(baseMsgStr, RootJson.class);
            Map<String, Object> map = new HashMap<>();
            map.put("proxyIpMac", proxyIpMac);
            map.put("cmdParamStartTask", root.getCMD_PARAM());
            TaskProcessResultHandler.getProcessResultQueue().add(map);
        }
    }

    /**
     * 接收回传的测试结果
     */
    public void receviceTestResult(String baseMsgStr) {
        MsgHead<TestResultInfo> testResultRoot = JSON.parseObject(baseMsgStr, new TypeReference<MsgHead<TestResultInfo>>(){});
        TaskProcessResultHandler.getProcessResultQueue().add(testResultRoot.getCMD_PARAM());
    }

    /**
     *  获取用例当前执行的信息
     */
    public void changeTask(ChannelHandlerContext ctx, String baseMsgStr) throws Exception
    {
        MsgHead<CurrentExecuteCase> deviceTask = JSON.parseObject(baseMsgStr,
                new TypeReference<MsgHead<CurrentExecuteCase>>() {
                });
        DeviceService deviceService = SpringContextHolder.getBean("deviceService");

        LOGGER.info("changeTask info is: " + baseMsgStr);

        deviceService.changeTask(deviceTask.getCMD_PARAM());
    }

    /**
     * 获取脚本执行到第几步的信息
     */
    public void changeStep(ChannelHandlerContext ctx, String baseMsgStr) throws Exception
    {
        MsgHead<CurrExecuteStep> stepInfo = JSON.parseObject(baseMsgStr, new TypeReference<MsgHead<CurrExecuteStep>>(){});
        AppCaseDeviceService appCaseDeviceService = SpringContextHolder.getBean("appCaseDeviceService");

        appCaseDeviceService.changeStep(stepInfo.getCMD_PARAM());
    }

    /**
     * 校验短信内容
     */
    public void verifySmsContent(String baseMsgStr)
    {
        MsgHead<VerifySmsMsg> verifySmsMsg = JSON.parseObject(baseMsgStr,
                new TypeReference<MsgHead<VerifySmsMsg>>() {
                });

        LOGGER.info("短信校验内容： " + verifySmsMsg.getCMD_PARAM().toString());

        VerifySmsMsg smsMsg = verifySmsMsg.getCMD_PARAM();
        if (null != smsMsg && StringUtils.isNotEmpty(smsMsg.getContent()))
        {
            String m = smsMsg.getContent();
            /*if (m.contains("动态密码") || m.contains("短信码") || m.contains("短信随机码") || m.contains("验证码") || (m.contains("短信授权码") && m.contains("用户模拟测试平台渠道")))
            {
                // 获取手机号码的授权码短息
                DeviceService deviceService = SpringContextHolder.getBean("deviceService");
                deviceService.saveSmsMsg(smsMsg);
            }
            else
            {*/
                AppScriptStepService appScriptStepService = SpringContextHolder.getBean("appScriptStepService");
                appScriptStepService.verifySmsContent(smsMsg);
            //}
            // 获取手机号码的授权码短息
            DeviceService deviceService = SpringContextHolder.getBean("deviceService");
            deviceService.saveSmsMsg(smsMsg);
        }
    }

    /**
     * 安装应用
     */
    public void installApk(ChannelHandlerContext ctx, String baseMsgStr)
    {
        MsgHead<AppOperationResponse> resMsgHead = JSON.parseObject(baseMsgStr, new TypeReference<MsgHead<AppOperationResponse>>() {});
        AppOperationResponse res = resMsgHead.getCMD_PARAM();

        AppService appService = SpringContextHolder.getBean("appService");
        AppApk apk = new AppApk();
        apk.setApkVersion(res.getAppVersion());
        apk.setPkgName(res.getPackageName());
        apk = appService.getApp(apk);

        ApkMessage msg = new ApkMessage();
        msg.setDeviceSn(res.getDeviceSn());
        msg.setAppName(apk.getApkName());
        msg.setResult(res.getResult());
        msg.setPackageName(res.getPackageName());
        msg.setAppVersion(res.getAppVersion());
        msg.setMsg(res.getMsg());
        msg.setType("0");

        DeviceWebSocketServer.sendApkMsgToPage(msg);
    }

    /**
     * 卸载应用
     */
    public void uninstallApk(ChannelHandlerContext ctx, String baseMsgStr)
    {
        MsgHead<AppOperationResponse> resMsgHead = JSON.parseObject(baseMsgStr, new TypeReference<MsgHead<AppOperationResponse>>() {});
        AppOperationResponse res = resMsgHead.getCMD_PARAM();

        AppService appService = SpringContextHolder.getBean("appService");
        AppApk apk = new AppApk();
        apk.setApkVersion(res.getAppVersion());
        apk.setPkgName(res.getPackageName());
        apk = appService.getApp(apk);

        ApkMessage msg = new ApkMessage();
        msg.setDeviceSn(res.getDeviceSn());
        msg.setAppName(apk.getApkName());
        msg.setResult(res.getResult());
        msg.setPackageName(res.getPackageName());
        msg.setAppVersion(res.getAppVersion());
        msg.setMsg(res.getMsg());
        msg.setType("1");

        DeviceWebSocketServer.sendApkMsgToPage(msg);
    }


}
