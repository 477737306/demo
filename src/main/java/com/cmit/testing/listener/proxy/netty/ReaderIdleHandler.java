package com.cmit.testing.listener.proxy.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/31 0031 下午 12:51.
 */
public class ReaderIdleHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderIdleHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent)
        {
            IdleStateEvent ev = (IdleStateEvent) evt;
            if (ev.state() == IdleState.READER_IDLE)
            {
                String proxy = "";
                Map<String, SocketChannel> map = NettyChannelMap.getChannelMap();
                for (Map.Entry entry : map.entrySet()) {
                    if (ctx.channel().equals(entry.getValue())) {
                        proxy = (String) entry.getKey();
                    }
                }
                LOGGER.error("Proxy " + proxy + " 发送心跳超时");
                ctx.channel().close();
                ctx.close();
            }
            else if (ev.state() == IdleState.WRITER_IDLE)
            {
                LOGGER.info("WRITER_IDLE 写超时");
            }
        }
        else
        {
            super.userEventTriggered(ctx, evt);
        }
    }

}
