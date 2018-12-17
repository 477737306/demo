package com.cmit.testing.listener.proxy.netty;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/11.
 */
public class ImageChannelMap {

    /**
     * 启动Server端对象
     */
    private static Map<String, NettyServerUDP> serverMap = new ConcurrentHashMap<String, NettyServerUDP>();

    /**
     * proxy连接后台服务传输图片流socket的连接对象。(tcp方式连接，udp没有连接对象)
     */
    private static Map<String, SocketChannel> channelMap = new ConcurrentHashMap<String, SocketChannel>();

    public static void addServerBootstrap(String clientId, NettyServerUDP serverBootstrap) {
        serverMap.put(clientId, serverBootstrap);
    }

    public static void addChannel(String clientId, SocketChannel socketChannel) {
        channelMap.put(clientId, socketChannel);
    }

    public static Channel getChannel(String clientId) {
        return channelMap.get(clientId);
    }


    public static NettyServerUDP getServerBootstrap(String port) {
        return serverMap.get(port);
    }

    public static void removeChannel(SocketChannel socketChannel) {
        for (Map.Entry entry : channelMap.entrySet()) {
            if (entry.getValue() == socketChannel) {
                channelMap.remove(entry.getKey());
            }
        }
    }

    public static Map<String, SocketChannel> getChannelMap() {
        return channelMap;
    }


    public static Map<String, NettyServerUDP> getServerBootstrapMap() {
        return serverMap;
    }
}
