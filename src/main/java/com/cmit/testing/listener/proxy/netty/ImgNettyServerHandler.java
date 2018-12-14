package com.cmit.testing.listener.proxy.netty;

import com.cmit.testing.listener.proxy.InitImagePushHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by zhangxiaofang on 2018/10/13.
 */
@Sharable
public class ImgNettyServerHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImgNettyServerHandler.class);

    private int serverPort;

    public ImgNettyServerHandler(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * 连接断开时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("图片传输连接断开！");
        String imei = "";
        Map<String, SocketChannel> map = ImageChannelMap.getChannelMap();
        for (Map.Entry entry : map.entrySet()) {
            if (ctx.channel().equals(entry.getValue())) {
                imei = (String) entry.getKey();
            }
        }
        ImageChannelMap.removeChannel((SocketChannel) ctx.channel());
        ctx.channel().close();

        // 断开连接时将队列中新增一张黑屏图片
        if (!"".equals(imei)) {
            String rootPath = getClass().getResource("/").getFile().toString().replace("/WEB-INF/classes/", "/images/black.jpg");
            LOGGER.debug(rootPath);
            if (image2byte(rootPath) != null) {
                Object[][] o = new Object[][] { { imei, image2byte(rootPath) } };
                InitImagePushHandler.getImgQueue().add(o);
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] baseMsg) throws Exception {
        try {
            if (baseMsg.length > 15) {
                byte[] deviceSnByte = Arrays.copyOfRange(baseMsg, 0, 15);
                String deviceSn = new String(deviceSnByte);
                if (!ImageChannelMap.getChannelMap().containsKey(deviceSn)) {
                    ImageChannelMap.addChannel(deviceSn, (SocketChannel) ctx.channel());
                }
                byte[] imgLength = Arrays.copyOfRange(baseMsg, 15, 23);
                Long imgL = bytes2long(imgLength);
                baseMsg = Arrays.copyOfRange(baseMsg, 23, baseMsg.length);
                LOGGER.debug("去掉头信息图像长度=" + baseMsg.length);
                if (imgL == baseMsg.length) {
                    // 将信图片息推送给执行线程
                    Object[][] o = new Object[][] { { deviceSn, baseMsg } };
                    InitImagePushHandler.getImgQueue().add(o);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("处理真机图片信息异常",e);
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(baseMsg);
        }
    }

    /**
     * 有客户端建立连接被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress inetSocketAddress = null;
        Channel channel = ctx.channel();
        if (channel != null) {
            inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        }
        LOGGER.info("proxy " + inetSocketAddress + "已经与server " + serverPort + " 端口建立连接！");
    }

    /**
     * 异常时被调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage());
        cause.getStackTrace();
    }

    public void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals(""))
            return;// 判断输入的byte是否为空
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));// 打开输入流
            imageOutput.write(data, 0, data.length);// 将byte写入硬盘
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }

    public static long bytes2long(byte[] b) {
        long temp = 0;
        long res = 0;
        for (int i = 0; i < 8; i++) {
            res <<= 8;
            temp = b[i] & 0xff;
            res |= temp;
        }
        return res;
    }

    public byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            input = new FileImageInputStream(new File(path));
            output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

}
