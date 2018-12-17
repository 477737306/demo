package com.cmit.testing.fastdfs.pool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/29 0029 下午 5:47.
 */
public class FastdfsClientFactory extends BasePoolableObjectFactory{

    private static final Logger LOGGER = LoggerFactory.getLogger(FastdfsClientFactory.class);

    static {
        InputStream is = null;
        File file = null;
        try {
            is = FastdfsClientFactory.class.getResourceAsStream("/config/fastdfs_client.conf");
            file  = new File("fastdfs_client.conf");
            FileUtils.copyInputStreamToFile(is,file);
            ClientGlobal.init(file.getPath());
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }finally {
            FileUtils.deleteQuietly(file);
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    public Object makeObject() throws Exception {
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageClient1 client = new StorageClient1(trackerServer, null);
        return client;
    }
}
