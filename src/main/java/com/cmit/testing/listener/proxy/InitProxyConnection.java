package com.cmit.testing.listener.proxy;

import com.cmit.testing.listener.proxy.netty.NettyServer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import org.slf4j.Logger;

/**
 * @author XieZuLiang
 * @description TODO 启动Netty服务端
 *
 *  InitializingBean接口为bean提供了初始化执行的方法，即afterPropertiesSet方法，
 *  凡是继承该接口的类，在初始化bean的时候会执行该方法。
 *
 * @date 2018/8/31 0031 下午 2:20.
 */
@Component
@ConfigurationProperties(prefix = "myproxy")
public class InitProxyConnection implements InitializingBean, ServletContextAware{

    private static final Logger LOGGER = LoggerFactory.getLogger(InitProxyConnection.class);

    /**
     * Server port
     */
    private String socketPort;

    /**
     * 与Proxy分隔符
     */
    private String split;

    /**
     * 心跳间隔时间内，没有收到信息认为proxy异常
     */
    private String readerIdleTime;

    @Override
    public void afterPropertiesSet() {
        boolean startSreverFlag = false;
        while (true) {
            try {
                if (startSreverFlag)
                {
                    break;
                }
                new NettyServer(Integer.parseInt(socketPort), split, Integer.parseInt(readerIdleTime));
                startSreverFlag = true;
                if (!startSreverFlag)
                {
                    Thread.sleep(10000);
                }
                LOGGER.info("Socket 服务端启动成功。端口：" + socketPort);
            } catch (Exception e) {
                LOGGER.error("Socket 服务端启动失败。", e);
            }
        }
    }

    public String getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getReaderIdleTime() {
        return readerIdleTime;
    }

    public void setReaderIdleTime(String readerIdleTime) {
        this.readerIdleTime = readerIdleTime;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {

    }
}
