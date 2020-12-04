package com.ldongxu.pdf2html.job;

import com.bj58.fanglearning.pdf2html.util.DownloadFile;
import com.bj58.fanglearning.pdf2html.util.RedisManager;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author liudongxu06
 * @date 2017/12/18
 */
public class DownloadRunner implements Runnable {
    private final static ExecutorService downloadService = Executors.newFixedThreadPool(8);
    private String redis_key;

    public DownloadRunner(String redis_key) {
        this.redis_key = redis_key;
    }

    @Override
    public void run() {
        doDownload(redis_key);
    }

    private void doDownload(String redis_key) {
        while (true) {
            Jedis jedis = null;
            try {
                jedis = RedisManager.getConnection();
                if (jedis == null) {
                    System.err.println("获取redis连接失败！");
                    TimeUnit.SECONDS.sleep(1);
                    continue;
                }
                List<String> list = jedis.brpop(3, redis_key);
                if (list == null || list.isEmpty()) {
                    continue;
                }
                String file = list.get(1);
                downloadService.execute(new DownloadFile(file));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                RedisManager.close(jedis);
            }

        }
    }
}
