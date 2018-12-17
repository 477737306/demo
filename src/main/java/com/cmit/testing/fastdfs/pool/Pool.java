package com.cmit.testing.fastdfs.pool;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/29 0029 下午 5:42.
 */
public abstract class Pool {
    private static final Logger LOGGER = LoggerFactory.getLogger(FastdfsClientFactory.class);

    private final GenericObjectPool internalPool;

    public Pool(GenericObjectPool.Config poolConfig, PoolableObjectFactory factory)
    {
        this.internalPool = new GenericObjectPool(factory, poolConfig);
    }

    public StorageClient1 getResource() throws Exception{
        int times = 3;
        for(int i=0;i<times;i++){
            try {
                Object obj = this.internalPool.borrowObject();
                if(obj!=null){
                    return (StorageClient1)obj;
                }
            } catch (Exception e) {
                LOGGER.info("Could not get a resource from the fastdfs pool,try again "+i);
                LOGGER.warn(e.getMessage());
            }
        }
        throw new Exception("Get a resource from the fastdfs pool error!!!!");
    }

    public void returnResource(StorageClient resource) throws Exception
    {
        try
        {
            this.internalPool.returnObject(resource);
        }
        catch (Exception e)
        {
            throw new Exception("Could not return the resource to the pool", e);
        }
    }

    public void returnBrokenResource(StorageClient resource) throws Exception
    {
        try
        {
            this.internalPool.invalidateObject(resource);
        }
        catch (Exception e)
        {
            throw new Exception("Could not return the resource to the pool", e);
        }
    }

    public void destroy() throws Exception
    {
        try
        {
            this.internalPool.close();
        }
        catch (Exception e)
        {
            throw new Exception("Could not destroy the pool", e);
        }
    }
}
