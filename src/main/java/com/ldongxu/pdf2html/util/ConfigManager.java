package com.ldongxu.pdf2html.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liudongxu06
 * @date 2017/12/7
 */
public class ConfigManager {
    private static Config config = ConfigFactory.load();

    public static String getString(String key){
        return config.getString(key);
    }

    public static int getInt(String key){
        return config.getInt(key);
    }

    public static String getString(String key,String defaultValue){
        String value = getString(key);
        return StringUtils.isBlank(value)?defaultValue:value;
    }

    public static long getLong(String key){
        return config.getLong(key);
    }

}
