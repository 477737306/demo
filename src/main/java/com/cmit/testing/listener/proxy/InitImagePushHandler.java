package com.cmit.testing.listener.proxy;

import com.cmit.testing.listener.proxy.websocket.DeviceWebSocketServer;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author XieZuLiang
 * @description  启动推送图像线程
 * @date 2018/9/5 0005 下午 12:34.
 */
public class InitImagePushHandler implements InitializingBean, ServletContextAware{

    private static final Logger LOGGER = LoggerFactory.getLogger(InitImagePushHandler.class);

    private final String imageBase64Head = "data:image/jpg;base64,";

    /**
     * 存放图片byte[]数据 无界线程安全的先进先出的队列
     */
    private static ConcurrentLinkedQueue<Object[][]> imageQueue = new ConcurrentLinkedQueue<Object[][]>();

    public static ConcurrentLinkedQueue<Object[][]> getImgQueue() {
        return imageQueue;
    }

    @Override
    public void afterPropertiesSet() {
        LOGGER.info("------------------------- 启动推送图片线程 -------------------------");
        SendImageThread sendImageThread = new SendImageThread();
        Thread thread = new Thread(sendImageThread);
        thread.start();
    }

    public static ConcurrentLinkedQueue<Object[][]> getImageQueue() {
        return imageQueue;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {

    }

    /**
     * 图像推送线程
     */
    public class SendImageThread implements Runnable{

        public SendImageThread() {
        }

        @Override
        public void run() {
            while (true)
            {
                try
                {
                    if (imageQueue.isEmpty())
                    {
                        Thread.sleep(1);
                    }
                    //poll 获取并移除此队列的头，如果此队列为空，则返回 null
                    Object[][] imageArray = imageQueue.poll();

                    if (imageArray != null) {

                        String deviceSn = (String) imageArray[0][0];
                        byte[] imgByte = (byte[]) imageArray[0][1];

                        if (deviceSn != null && deviceSn.length() > 0 && imgByte != null && imgByte.length > 0)
                        {
                            Base64Encoder encoder = new Base64Encoder();
                            String imgBase64 = imageBase64Head + encoder.encode(imgByte);
                            DeviceWebSocketServer.sendMsgToPage(deviceSn, imgBase64);
                            LOGGER.debug("推送图片成功。deviceSn="+deviceSn);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    LOGGER.error("推送图片异常：", e);
                }
            }
        }
    }
}
