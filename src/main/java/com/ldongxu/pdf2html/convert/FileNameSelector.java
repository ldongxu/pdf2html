package com.ldongxu.pdf2html.convert;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author liudongxu06
 * @date 2017/12/18
 */
public class FileNameSelector implements FilenameFilter {
    private String extension;

    public FileNameSelector(String extension) {
        if (extension.startsWith(".")){
            this.extension = extension;
        }else {
            this.extension = "."+extension;
        }
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(extension.toLowerCase());
    }
}
