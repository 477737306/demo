package com.cmit.testing.listener.proxy.netty;

import com.cmit.testing.entity.proxy.ImageData;
import com.cmit.testing.listener.proxy.websocket.DeviceWebSocketServer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/9/11 0011.
 */
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UdpServerHandler.class);

    private  volatile  boolean hasData = false;

    private final  Object object = new Object();

    public static Cache<String,ImageData> getTempRepository()
    {
        return tempRepository;
    }

    //使用guava cache来暂存图像组装过程中的临时对象，这样可以不用担心对象清理，2分钟后自动清除
    private static final Cache<String,ImageData> tempRepository = CacheBuilder
            .newBuilder()
            .expireAfterWrite(50, TimeUnit.SECONDS)
            .build();


    private static final ConcurrentHashMap<String,Long> lastImages = new ConcurrentHashMap<>(10);
    private static final ConcurrentHashMap<String,Long> lastImagesArriveTimes = new ConcurrentHashMap<>(10);
    private static final ConcurrentHashMap<String,PriorityBlockingQueue<ImageData>> imageDataMap = new ConcurrentHashMap<>(50);

    public UdpServerHandler()
    {
        ImagePublisher publisher = new ImagePublisher(8);
        publisher.start();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket msg) {
        // 读取udp数据包，因为数据包到达顺序不一定，且有些数据包会不完整，所有这里判断数据完整的方式是，接收到所有数据包，
        // 且每个数据包都有效。对于出现异常数据包的图像，仍旧照常接收，但是不会去读数据，在接收到所有数据包后直接丢弃。
        // 对于中间丢包图像，只能等超时后被cache清理掉。
        synchronized (object)
        {
            try
            {
                if (msg != null && msg.content() != null)
                {
                    ByteBuf buf = msg.content();
                    if (buf.readableBytes() > 51)
                    {
                        // deviceSn 占据数据包19个字节
                        byte[] deviceSnBytes = new byte[19];
                        buf.readBytes(deviceSnBytes);
                        String deviceSn = new String(deviceSnBytes);
                        StringBuilder key = new StringBuilder(deviceSn);
                        long imgMarkKey = buf.readLong();
                        key.append(imgMarkKey);
                        //以key和图像id作为cache的key
                        ImageData imageData = tempRepository.getIfPresent(key.toString());
                        if (imageData == null || imageData.getData()==null)
                        {
                            imageData = new ImageData(deviceSn, buf.readLong(), imgMarkKey);
                            tempRepository.put(key.toString(), imageData);
                        }
                        else
                        {
                            //跳过总分块部分
                            buf.skipBytes(8);
                        }
                        readData(imageData, buf);
                        if (imageData.receivedAll())
                        {
                            PriorityBlockingQueue<ImageData> queue = imageDataMap.get(deviceSn);
                            if (queue == null) {
                                queue = new PriorityBlockingQueue<>(10);
                                imageDataMap.put(deviceSn, queue);
                            }
                            Long lastImage = lastImages.get(deviceSn);
                            if (lastImage == null)
                            {
                                setLastArriveImage(deviceSn, imgMarkKey);
                                queue.add(imageData);
                            }
                            else
                            {
                                Long lasImages = lastImages.get(deviceSn);
                                Long lastImagesArriveTime = lastImagesArriveTimes.get(deviceSn);
                                if (lasImages < imgMarkKey)
                                {
                                    queue.add(imageData);
                                    setLastArriveImage(deviceSn, imgMarkKey);
                                }
                                else if (lasImages > imgMarkKey && (System.currentTimeMillis() - lastImagesArriveTime) < 1000)
                                {
                                    queue.add(imageData);
                                }
                                else
                                {
                                    queue.add(imageData);
                                    LOGGER.warn("data arrive late: deviceSn: " + deviceSn + " imgMarkkey: " + imgMarkKey);
                                }
                            }
                            tempRepository.invalidate(key);
                            hasData = true;
                            object.notifyAll();
                        }

                    }

                }
            }
            catch (Exception e)
            {
                LOGGER.error("process data page error !", e);
            }
        }
    }

    private void setLastArriveImage(String imei,Long imgMarkKey)
    {
        Long lasImage = lastImages.get(imei);
        if(lasImage==null || lasImage<imgMarkKey)
        {
            lastImages.put(imei,imgMarkKey);
            lastImagesArriveTimes.put(imei,System.currentTimeMillis());
        }
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable cause)
    {
        LOGGER.error("receive data error !",cause);
    }

    /**
     * 把数据读到数组指定位置，因为接受到的数据报可能会乱码，因此这里不能顺序写数组
     * @param imageData
     * @param buf
     */
    private void readData(ImageData imageData,ByteBuf buf)
    {
        long currentParts = buf.readLong();
        imageData.setCurrentPart(currentParts);
        //只有之前没有出现过错误数据包的从会读取它的数据
        if(!imageData.isBadData())
        {
            int partNumber = (int) currentParts;
            long length = buf.readLong();
            //预期长度跟还能读的数据长度不一致
            if (length != buf.readableBytes())
            {
                LOGGER.error("所接收的数据不完整");
                imageData.badData();
            }
            else
            {
                int start = 1024 * (partNumber - 1);
                buf.readBytes(imageData.getData(), start, (int) length);
                imageData.setDataLength(length);
            }
        }
    }




    class ImagePublisher {

        private ExecutorService executor;

        private final Base64Encoder base64Encoder;

        private final String imgBase64Head = "data:image/jpg;base64,";


        private ImagePublisher(int threadNum)
        {
            executor = Executors.newFixedThreadPool(threadNum);
            base64Encoder = new Base64Encoder();
        }


        public void start()
        {
            executor.submit(new Runnable()
            {
                @Override
                public void run()
                {
                    LOGGER.info("ImagePublisher start ...");
                    final CopyOnWriteArraySet processingSet = new CopyOnWriteArraySet();
                    while (true)
                    {
                        synchronized (object)
                        {
                            while (!hasData)
                            {
                                try
                                {
                                    LOGGER.info("ImagePublisher waiting ...");
                                    object.wait();
                                } catch (InterruptedException e) {
                                    LOGGER.warn("wait error !");
                                }
                            }
                            hasData = false;
                            for (final String key : imageDataMap.keySet())
                            {
                                final PriorityBlockingQueue<ImageData> queue = imageDataMap.get(key);
                                if (processingSet.contains(key) || queue.isEmpty())
                                {
                                    continue;
                                }
                                processingSet.add(key);
                                LOGGER.info("start processing deviceSn is: " + key);
                                executor.submit(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        try
                                        {
                                            ImageData imageData = null;
                                            int counter = 0;
                                            do
                                            {
                                                imageData = queue.poll();
                                                if(imageData!=null) {
                                                    String sendData = imgBase64Head + base64Encoder.encode(Arrays.copyOf(imageData.getData(), (int) imageData.getDataLength()));
                                                    DeviceWebSocketServer.sendMsgToPage(imageData.getDeviceSn(), sendData);
                                                    imageData.setData(null);
                                                    imageData = null;
                                                }
                                                counter++;
                                            }
                                            while (counter<20 || imageData != null);
                                        }
                                        catch (Exception e)
                                        {
                                            LOGGER.error("push image error !",e);
                                        }
                                        finally
                                        {
                                            processingSet.remove(key);
                                        }

                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }
}
