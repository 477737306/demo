package com.cmit.testing.listener.proxy.netty;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XieZuLiang
 * @description TODO 管理proxy与后台长连接的对象
 * @date 2018/8/30 0030 下午 6:08.
 */
public class NettyChannelMap {

    /**
     * 用于保存连接到后台的服务proxy的socket的长连接对象
     */
    private static final Map<String, SocketChannel> map = new ConcurrentHashMap<String, SocketChannel>();



    public static Map<String, SocketChannel> getChannelMap() {
        return map;
    }

    public static void addChannel(String proxyip_mac, SocketChannel socketChannel){
        map.put(proxyip_mac, socketChannel);
    }

    public static Channel getChannel(String proxyip_mac) {
        return map.get(proxyip_mac);
    }

    public static void removeChannel(SocketChannel socketChannel) {
        for (Map.Entry entry : map.entrySet())
        {
            if (entry.getValue() == socketChannel)
            {
                map.remove(entry.getKey());
            }
        }
    }
}
