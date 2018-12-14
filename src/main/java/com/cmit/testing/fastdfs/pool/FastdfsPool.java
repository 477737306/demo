package com.cmit.testing.fastdfs.pool;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.stereotype.Component;

/**
 * @author XieZuLiang
 * @description TODO
 * @date 2018/8/29 0029 下午 5:48.
 */
public class FastdfsPool extends Pool {

    public FastdfsPool(){
        super(new FastdfsPoolConfig(), new FastdfsClientFactory());
    }

    public FastdfsPool(GenericObjectPool.Config poolConfig, PoolableObjectFactory factory)
    {
        super(poolConfig, factory);
    }

    public FastdfsPool(GenericObjectPool.Config poolConfig)
    {
        super(poolConfig, new FastdfsClientFactory());
    }
}
