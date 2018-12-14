package com.cmit.testing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author YangWanLi
 * @date 2018/7/7 19:20
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private Jedis jedis;

    private String host;

    private String auth;

    private int port;

    private int timeout;

    public static class Jedis {
        private Pool pool;

        public Jedis() {
        }

        public Pool getPool() {
            return this.pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }
        public static class Pool {
            private int maxIdle = 8;
            private int minIdle = 0;
            private int maxActive = 8;
            private Duration maxWait = Duration.ofMillis(-1L);

            public Pool() {
            }

            public int getMaxIdle() {
                return this.maxIdle;
            }

            public void setMaxIdle(int maxIdle) {
                this.maxIdle = maxIdle;
            }

            public int getMinIdle() {
                return this.minIdle;
            }

            public void setMinIdle(int minIdle) {
                this.minIdle = minIdle;
            }

            public int getMaxActive() {
                return this.maxActive;
            }

            public void setMaxActive(int maxActive) {
                this.maxActive = maxActive;
            }

            public Duration getMaxWait() {
                return this.maxWait;
            }

            public void setMaxWait(Duration maxWait) {
                this.maxWait = maxWait;
            }
        }
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
