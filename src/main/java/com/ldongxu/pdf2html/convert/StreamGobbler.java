package com.ldongxu.pdf2html.convert;

import java.io.*;

/**
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流
 * @author liudongxu06
 * @date 2017/12/5
 */
public class StreamGobbler extends Thread {
    private InputStream in;
    private String type;
    private OutputStream out;

    public StreamGobbler(InputStream in, String type, OutputStream out) {
        this.in = in;
        this.type = type;
        this.out = out;
    }

    public StreamGobbler(InputStream in, String type) {
        this(in,type,null);
    }

    @Override
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        if (out!=null)
            pw = new PrintWriter(out);
        isr = new InputStreamReader(in);
        br = new BufferedReader(isr);
        String line = null;
        try {
            while ((line = br.readLine())!=null){
                if (pw!=null)
                    pw.println(line);
                System.out.println(type + ">" + line);
            }
            if (pw!=null)
                pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pw!=null)
                    pw.close();
                if (br!=null)
                    br.close();
                if (isr!=null)
                    isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
