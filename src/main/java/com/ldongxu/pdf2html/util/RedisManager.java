package com.ldongxu.pdf2html.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * @author liudongxu06
 * @date 2017/12/6
 */
public class RedisManager {

    private static JedisPool pool;

    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(ConfigManager.getInt("redis.pool.max-idle"));
        poolConfig.setMaxTotal(ConfigManager.getInt("redis.pool.max-active"));
        poolConfig.setMaxWaitMillis(ConfigManager.getLong("redis.pool.max-wait"));
        poolConfig.setMinIdle(ConfigManager.getInt("redis.pool.min-idle"));
        String host = ConfigManager.getString("redis.host", "127.0.0.1");
        String port = ConfigManager.getString("redis.port", "6379");
        String password = ConfigManager.getString("redis.password");
        pool = new JedisPool(poolConfig, host, Integer.parseInt(port.trim()), 2000, password);
    }

    public static Jedis getConnection() {
        if (pool != null) {
            return pool.getResource();
        }
        throw new RuntimeException("获取redis连接资源失败");
    }

    public static void close(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }

}
