package com.ldongxu.pdf2html.util;

import com.ldongxu.pdf2html.convert.Pdf2htmlEXUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author liudongxu06
 * @date 2017/12/7
 */
public class Pdf2html implements Runnable {
    private File file;
    private String dest;
    private CountDownLatch countDownLatch;

    public Pdf2html(File file, String dest, CountDownLatch countDownLatch) {
        this.file = file;
        this.dest = dest;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            if (file==null || StringUtils.isBlank(dest)) {
                System.err.println("pdf文件或转换目标路径不能为空");
                return;
            }
            System.out.println("开始解析" + file.getAbsolutePath() + "到" + dest);
            boolean success = Pdf2htmlEXUtil.pdf2html(file.getAbsolutePath(), dest, null);//htmlFileName为空保持与源file同名
            if (success) {
                file.delete();
            }else {
                System.err.println(file + "转换html失败！");

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (countDownLatch != null)
                countDownLatch.countDown();
        }
    }


}
