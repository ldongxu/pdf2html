package com.ldongxu.pdf2html.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author liudongxu06
 * @date 2017/12/18
 */
public class DownloadFile implements Runnable {
    private String fileUrl;
    public static final String pdfPath = ConfigManager.getString("pdfFilePath");
    private static final String tmpSuffix = ".tmp";

    public DownloadFile(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    private void download() {
        System.out.println("开始下载:"+fileUrl);
        String tmp;
        String filename = getFileName(fileUrl);
        if (pdfPath.endsWith("/") || pdfPath.endsWith("\\")) {
            tmp = pdfPath + filename + tmpSuffix;
        } else {
            tmp = pdfPath + "/" + filename + tmpSuffix;
        }
        System.out.println("临时文件："+tmp);
        if (downloadHttpFile(fileUrl, tmp)) {
            renameTmp(tmp);
        }

    }

    private boolean downloadHttpFile(String fileUrl, String savePath) {
        int byteSum = 0;
        int byteRead = 0;
        InputStream inputStream = null;
        FileOutputStream out = null;
        try {
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            out = new FileOutputStream(savePath);
            byte[] buffer = new byte[1024];
            while ((byteRead = inputStream.read(buffer)) != -1) {
                byteSum += byteRead;
                out.write(buffer, 0, byteRead);
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean renameTmp(String tmp) {
        String file = tmp.substring(0, tmp.lastIndexOf(tmpSuffix));
        System.out.println("下载完成："+file);
        return new File(tmp).renameTo(new File(file));
    }

    private String getFileName(String url) {
        if (StringUtils.isBlank(url)) return "";
        int start = url.lastIndexOf("/");
        String extension =  url.substring(start + 1);
        int index = extension.indexOf("?");
        if (index!=-1){
            extension = extension.substring(0,index);
        }
        return extension;
    }

    @Override
    public void run() {
        download();
    }

}
