package com.cmit.testing.utils;

import com.cmit.testing.config.RedisConfig;
import com.github.pagehelper.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Component
public class RedisUtil implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisConfig redisConfig;

    private static RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        redisUtil = this;
        redisUtil.redisConfig = this.redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }


    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;


    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 获取redis键值-object
     *
     * @param key
     * @return
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            logger.error("setObject设置redis键值异常:key=" + key + " value=" + value + " cause:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 获取redis键值-object
     *
     * @param key
     * @return
     */
    public static String set(String key, String value, Long expireSecond) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            boolean keyExist = jedis.exists(key);
            // NX是不存在时才set， XX是存在时才set， EX是秒，PX是毫秒
            if (keyExist) {
                jedis.del(key);
            }
            return jedis.set(key, value, "NX", "EX", expireSecond);
        } catch (Exception e) {
            logger.error("setObject设置redis键值异常:key=" + key + " value=" + value + " cause:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 获取redis键值-object
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("getObject获取redis键值异常:key=" + key + " cause:" + e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 获取redis键值-object
     *
     * @param key
     * @return
     */
    public static Object getObject(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(key.getBytes());
            if (null != bytes && bytes.length > 0) {
                return SerializationUtils.deserialize(bytes);
            }
        } catch (Exception e) {
            logger.error("getObject获取redis键值异常:key=" + key + " cause:" + e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 设置redis键值-object
     *
     * @param key
     * @param value
     * @return
     */
    public static String setObject(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key.getBytes(), SerializationUtils.serialize(value));
        } catch (Exception e) {
            logger.error("setObject设置redis键值异常:key=" + key + " value=" + value + " cause:" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static String setObject(String key, Object value, int expiretime) {
        String result = "";
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.set(key.getBytes(), SerializationUtils.serialize(value));
            if (result.equals("OK")) {
                jedis.expire(key.getBytes(), expiretime);
            }
            return result;
        } catch (Exception e) {
            logger.error("setObject设置redis键值异常:key=" + key + " value=" + value + " cause:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 删除key
     */
    public static Long delkeyObject(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.del(key.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static Boolean existsObject(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 设置hash数据类型
     */
    public static Long hset(String key, String item, Object value) {
        if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return jedis.hset(key.getBytes(), item.getBytes(), SerializationUtils.serialize(value));
            } catch (Exception e) {
                return null;
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        } else {
            return null;
        }
    }


    /**
     * 获取hash数据类型
     */
    public static Object hget(String key, String item) {
        if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return SerializationUtils.deserialize(jedis.hget(key.getBytes(), item.getBytes()));
            } catch (Exception e) {
                return null;
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        } else {
            return null;
        }
    }

    public static Long incr(String key) {
        if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return jedis.incr(key);
            } catch (Exception e) {
                return -1L;
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        } else {
            return -1L;
        }
    }

    public static Long incrBy(String key, long increment) {
        if (StringUtil.isNotEmpty(key)) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return jedis.incrBy(key, increment);
            } catch (Exception e) {
                return -1L;
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        } else {
            return -1L;
        }
    }

    @Override
    public void afterPropertiesSet() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            RedisConfig.Jedis.Pool pool = redisConfig.getJedis().getPool();
            config.setMaxTotal(pool.getMaxActive());
            config.setMaxIdle(pool.getMaxIdle());
            config.setMaxWaitMillis(pool.getMaxWait().getSeconds());
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout(), redisConfig.getAuth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RedisConfig getRedisConfig() {
        return redisUtil.redisConfig;
    }
}