package com.uppc.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by songlinwei on 16/4/22.
 */
public class PropertyUtil {
    private final static String PATH_APP_CONFIG = "config";

    private Context context;

    public PropertyUtil(Context c) {
        context = c;
    }

    public String loadString(String key) {
        Properties props = loadProperties();
        return (props != null) ? props.getProperty(key) : null;
    }

    public void saveString(String key, String value) {
        Properties props = loadProperties();
        props.setProperty(key, value);
        persistProperties(props);
    }

    public void remove(String... key) {
        Properties props = loadProperties();
        for (String k : key) {
            props.remove(k);
        }
        persistProperties(props);
    }

    private Properties loadProperties() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取app_config目录下的config
            File dirConf = getContext().getDir(PATH_APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator + PATH_APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void persistProperties(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在(自定义)app_config的目录下
            File dirConf = getContext().getDir(PATH_APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, PATH_APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    private Context getContext()
    {
        return context;
    }
}
