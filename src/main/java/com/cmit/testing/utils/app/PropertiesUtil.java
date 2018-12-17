package com.cmit.testing.utils.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author XieZuLiang
 * @description
 * @date 2018/10/8 0008.
 */
public class PropertiesUtil {

    public static String getValue(String filePath, String key){
        Properties properties = new Properties();
        try(InputStream in = new FileInputStream(filePath)){
            properties.load(in);
            return properties.getProperty(key);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }


    public static String getValue(InputStream in, String key){
        Properties properties = new Properties();
        try{
            properties.load(in);
            return properties.getProperty(key);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
