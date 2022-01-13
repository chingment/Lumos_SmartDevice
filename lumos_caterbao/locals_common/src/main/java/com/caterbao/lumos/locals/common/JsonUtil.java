package com.caterbao.lumos.locals.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.List;

public class JsonUtil<T> {
    public static  String getJson(Object object){

        String str =null;
        //jackson中的方法用于将对象转换为字符串形式
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //将对象装换为json形式
            str = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return  str;
    }

    public static  String getJson(List object) {

        if (object == null)
            return null;
        if (object.size() <= 0)
            return null;

        String str = null;
        //jackson中的方法用于将对象转换为字符串形式
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //将对象装换为json形式
            str = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getJson(Object object, String dateFormat) {
        String str = null;
        //jackson中的方法用于将对象转换为字符串形式
        ObjectMapper objectMapper = new ObjectMapper();
        //关闭时间戳功能，如果不关闭时将时间直接json化时将显示的是时间戳不是json形式的字符串
        ObjectMapper configure = objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        设置时间格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
//        设置json字符串的世界格式显示
        configure.setDateFormat(simpleDateFormat);
        try {
            //将对象装换为json形式
            str = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Object toObject(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object obj = objectMapper.readValue(json,
                    new TypeReference<Object>() {
                    });
            return obj;
        }
        catch (Exception ex){
            return  null;
        }
    }
}
