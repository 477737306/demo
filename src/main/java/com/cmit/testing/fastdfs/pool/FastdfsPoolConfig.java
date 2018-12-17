package com.cmit.testing.fastdfs.pool;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author XieZuLiang
 * @description TODO Fastdfs配置
 * @date 2018/8/29 0029 下午 5:00.
 */
@Component
@ConfigurationProperties(prefix = "fastdfs")
public class FastdfsPoolConfig extends GenericObjectPool.Config{

    private Integer maxActive;

    private Integer minIdle;

    public FastdfsPoolConfig()
    {
        setTestWhileIdle(true);
        setMaxActive(2);
        setMinIdle(0);
        setMaxWait(30000L);
        setTestOnBorrow(true);
        // 设定在进行后台对象清理时，休眠时间超过了毫秒的对象为过期
        setMinEvictableIdleTimeMillis(120000L);
        // 间隔每过多少毫秒进行一次后台对象清理的行动
        setTimeBetweenEvictionRunsMillis(90000L);
        //－1表示清理时检查所有线程
        setNumTestsPerEvictionRun(-1);
    }

    public Integer getMaxIdle() {
        return this.maxIdle;
    }

    public void setMaxIdle(Integer maxIdle)
    {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle()
    {
        return this.minIdle;
    }

    public void setMinIdle(Integer minIdle)
    {
        this.minIdle = minIdle;
    }

    public Integer getMaxActive()
    {
        return this.maxActive;
    }

    public void setMaxActive(Integer maxActive)
    {
        this.maxActive = maxActive;
    }

    public Long getMaxWait()
    {
        return this.maxWait;
    }

    public void setMaxWait(Long maxWait)
    {
        this.maxWait = maxWait;
    }

    public byte getWhenExhaustedAction()
    {
        return this.whenExhaustedAction;
    }

    public void setWhenExhaustedAction(byte whenExhaustedAction)
    {
        this.whenExhaustedAction = whenExhaustedAction;
    }

    public boolean isTestOnBorrow()
    {
        return this.testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow)
    {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn()
    {
        return this.testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn)
    {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle()
    {
        return this.testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle)
    {
        this.testWhileIdle = testWhileIdle;
    }

    public Long getTimeBetweenEvictionRunsMillis()
    {
        return this.timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis)
    {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public Integer getNumTestsPerEvictionRun()
    {
        return this.numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun)
    {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public Long getMinEvictableIdleTimeMillis()
    {
        return this.minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis)
    {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public Long getSoftMinEvictableIdleTimeMillis()
    {
        return this.softMinEvictableIdleTimeMillis;
    }

    public void setSoftMinEvictableIdleTimeMillis(Long softMinEvictableIdleTimeMillis)
    {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

}
