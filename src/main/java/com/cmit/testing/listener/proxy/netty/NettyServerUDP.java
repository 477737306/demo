package com.cmit.testing.listener.proxy.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/11 0011.
 */
public class NettyServerUDP {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerUDP.class);

    /**
     * 端口
     */
    private int port;

    /**
     * 分隔符
     */
    private static final byte[] IMAGE_PACKAGE_HEAD = { (byte) 0xFF, (byte) 0xCF, (byte) 0xFA, (byte) 0xBF, (byte) 0xF6,
            (byte) 0xAF, (byte) 0xFE, (byte) 0xFF, (byte) 0x0E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x41,
            (byte) 0x73, (byte) 0x70, (byte) 0x69, (byte) 0x72, (byte) 0x65, (byte) 0x5F, (byte) 0x4D, (byte) 0x69,
            (byte) 0x6E, (byte) 0x69, (byte) 0x63, (byte) 0x61, (byte) 0x70 };

    public NettyServerUDP(int port)
    {
        this.port = port;
        ServerUDPThread serverUDPThread = new ServerUDPThread();
        Thread thread = new Thread(serverUDPThread);
        thread.start();
        LOGGER.info("------------------图片传输UDP Server启动-----------------------");
    }

    public class ServerUDPThread implements Runnable {

        public ServerUDPThread() {
        }

        @Override
        public void run() {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group);
                bootstrap.channel(NioDatagramChannel.class);
                bootstrap.localAddress(port);
                bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 1024*10);
                bootstrap.option(ChannelOption.SO_BROADCAST, true);
                bootstrap.handler(new UdpServerHandler());
                Channel channel = bootstrap.bind(port).sync().channel();
                channel.closeFuture().await();
            } catch (InterruptedException e) {
                ImageChannelMap.getServerBootstrapMap().remove(port + "");
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }
    }

    private void bind() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioDatagramChannel.class);
            bootstrap.localAddress(port);
            bootstrap.option(ChannelOption.SO_BROADCAST, true);
            bootstrap.handler(new UdpServerHandler());

            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
