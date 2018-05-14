package com.deepthoughtdata.util;

import com.deepthoughtdata.entity.UploadConifg;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class UploadConfigUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext=null;

    private static Map<String ,String> propertiesMap=null;

    public UploadConfigUtil() {
    }

    public static String getConfigByKey(String key) {
        if(propertiesMap==null){
            UploadConifg uploadConifg = applicationContext.getBean(UploadConifg.class);
            propertiesMap=uploadConifg.getMapProps();
        }
        return propertiesMap.get(key);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(UploadConfigUtil.applicationContext==null){
            UploadConfigUtil.applicationContext=applicationContext;
        }
    }
}
