package com.ldongxu.pdf2html.job;

import com.ldongxu.pdf2html.convert.FileNameSelector;
import com.ldongxu.pdf2html.util.DownloadFile;
import com.ldongxu.pdf2html.util.Pdf2html;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liudongxu06
 * @date 2017/12/18
 */
public class Pdf2htmlRunner implements Runnable{
    private final static ExecutorService pdf2htmlService = Executors.newFixedThreadPool(8);
    private String dest;

    public Pdf2htmlRunner(String dest) {
        this.dest = dest;
    }
    @Override
    public void run() {
        doPdf2html(dest);
    }

    private void doPdf2html(String dest) {
        while (true) {
            try {
                File pdfDir = new File(DownloadFile.pdfPath);
                if (pdfDir.exists() && pdfDir.isDirectory()) {
                    File[] files = pdfDir.listFiles(new FileNameSelector("pdf"));
                    if (files == null) {
                        Thread.sleep(3000);
                        continue;
                    }
                    CountDownLatch countDownLatch = new CountDownLatch(files.length);//这次扫描转换完成之后再进行下次扫描
                    Arrays.stream(files).filter(File::isFile).forEach(f->pdf2htmlService.execute(new Pdf2html(f,dest,countDownLatch)));
//                    for (File file : files) {
//                        if (file.isFile()) {
//                            Pdf2html runner = new Pdf2html(file, dest, countDownLatch);
//                            pdf2htmlService.execute(runner);
//                        }
//                    }
                    countDownLatch.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
