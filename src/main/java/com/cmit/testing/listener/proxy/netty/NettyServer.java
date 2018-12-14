package com.cmit.testing.listener.proxy.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author XieZuLiang
 * @description TODO Netty服务端
 * @date 2018/8/31 0031 上午 10:14.
 */
public class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    /**
     * 端口
     */
    private int port;
    /**
     * 分隔符
     */
    private String split;

    /**
     * 读取消息超时时间（即测试端一定时间内未接受到被测试端消息）
     */
    private int readerIdleTime;

    public NettyServer(int port, String split, int readerIdleTime) throws InterruptedException {
        this.port = port;
        this.split = split;
        this.readerIdleTime = readerIdleTime;
        
        bind();
    }

    private void bind() throws InterruptedException {
        //实例化两个线程组NioEventLoopGroup，一个是父线程（Boss线程），一个是子线程(worker线程)
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        //实例化Netty服务器启动引导程序
        ServerBootstrap bootstrap = new ServerBootstrap();
        //设置启动的线程组
        bootstrap.group(boss, worker);
        //设置Channel类型
        bootstrap.channel(NioServerSocketChannel.class);

        //设置可连接队列的大小
        bootstrap.childOption(ChannelOption.SO_BACKLOG, 128);
        //禁止使用Nagle算法
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        //保持长连接
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        //设置责任链路
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                ChannelPipeline channelPipeline = socketChannel.pipeline();
                //在服务器端会每隔readerIdleTime秒来检查一下channelRead方法被调用的情况，
                //如果在readerIdleTime秒内该链上的channelRead方法都没有被触发，就会调用userEventTriggered方法
                channelPipeline.addLast("idleStateHandler", new IdleStateHandler(readerIdleTime, 0, 0, TimeUnit.SECONDS));
                //自定义读写超时的处理
                channelPipeline.addLast("myHandler", new ReaderIdleHandler());
                // 创建分隔符缓冲对象
                ByteBuf delimiter = Unpooled.copiedBuffer(split.getBytes());
                // 当达到最大长度仍没找到分隔符 就抛出异常  //最大1M
                socketChannel.pipeline().addLast("delimiter", new DelimiterBasedFrameDecoder(1024*2048, true, delimiter));
                //
                socketChannel.pipeline().addLast("byteArraydecoder", new ByteArrayDecoder());
                socketChannel.pipeline().addLast("byteArrayencoder", new ByteArrayEncoder());
                channelPipeline.addLast(new NettyServerHandler(port,split));
            }
        });

        //绑定端口并监听
        ChannelFuture future = bootstrap.bind(port).sync();
        if (future.isSuccess())
        {
            LOGGER.info("----------------- Netty Server started port is " + port + ". -----------------");
        }
    }
}
