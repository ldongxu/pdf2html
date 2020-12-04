package com.ldongxu.pdf2html.convert;

import com.ldongxu.pdf2html.util.DownloadFile;
import org.apache.commons.lang3.StringUtils;


/**
 * pdf转换html
 * 需要依赖bwits/pdf2htmlex的docker镜像
 * @author liudongxu06
 * @date 2017/12/5
 */
public class Pdf2htmlEXUtil {

    private final static String OS = System.getProperty("os.name");
    private final static String EXE_PATH = System.getProperty("user.dir") + "/pdf2htmlEXE/";

    public static boolean pdf2html(String pdfFile, String destDir, String htmlFileName) {
        if (StringUtils.isBlank(pdfFile)) {
            System.err.println("传递的参数有误！");
            return false;
        }
        if (StringUtils.isNotBlank(destDir)){
            destDir = destDir.replace(" ", "\" \"");
        }
        Runtime rt = Runtime.getRuntime();
        StringBuilder command = new StringBuilder();
        if (isUnix()) {
            command.append("docker run -i --rm ");
            command.append("-v "+ DownloadFile.pdfPath+":"+DownloadFile.pdfPath);
            if (StringUtils.isNotBlank(destDir)){
                command.append(" -v "+destDir+":"+destDir);
            }
            command.append(" bwits/pdf2htmlex ");
            command.append("pdf2htmlEX").append(" ");
        } else if (isWin()) {
            command.append(EXE_PATH).append("pdf2htmlEX").append(" ");
        }
        if (StringUtils.isNotBlank(destDir)) {
            command.append("--dest-dir ").append(destDir).append(" ");
        }
        command.append("--fit-width 375 ");
//        command.append("--zoom 1.3 ");
        command.append("--split-pages 1 ");
//        command.append("--bg-format jpg ");
        command.append(pdfFile.replace(" ", "\" \"")).append(" ");//需要替换文件路径中的空格
        if (StringUtils.isNotBlank(htmlFileName)) {
            command.append(htmlFileName);
            if (htmlFileName.indexOf(".html") == -1)
                command.append(".html");
        }
        System.out.println("Command：" + command.toString());
        try {
            Process process = rt.exec(command.toString());
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
            StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
            errorGobbler.start();
            outGobbler.start();
            int w = process.waitFor();
            int v = process.exitValue();
            if (w == 0 && v == 0) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    private static boolean isWin() {
        return OS.toLowerCase().contains("win");
    }

    private static boolean isUnix() {
        String lower = OS.toLowerCase();
        return lower.contains("nix") || lower.contains("nux") || lower.contains("aix") || lower.contains("mac");
    }


}
