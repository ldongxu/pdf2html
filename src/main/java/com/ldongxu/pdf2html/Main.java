package com.ldongxu.pdf2html;

import com.ldongxu.pdf2html.job.DownloadRunner;
import com.ldongxu.pdf2html.job.Pdf2htmlRunner;
import com.ldongxu.pdf2html.util.ConfigManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liudongxu06
 * @date 2017/12/7
 */
public class Main {

    private final static String JOB_REDIS_KEY = "pdf2html_redis_key";//redis默认key
    private final static String DEST_DEFAULT = ConfigManager.getString("htmlFilePath");//生成的html默认存储路径
    private static ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        String dest = DEST_DEFAULT;
        String redis_key = JOB_REDIS_KEY;
        if (args != null && args.length > 0) {
            dest = args[0];
            if (args.length > 1) {
                redis_key = args[1];
            }

        }
        executor.execute(new DownloadRunner(redis_key));
        executor.execute(new Pdf2htmlRunner(dest));
    }
}
