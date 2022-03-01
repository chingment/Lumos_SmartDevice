package com.caterbao.lumos.locals.common;


import net.sourceforge.pinyin4j.PinyinHelper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CommonUtil {
    public  static String HelloWorld(){

        System.out.print("HelloWorld");
        return "HelloWorld";
    }

    public static Timestamp getDateTimeNow(){
        return  new Timestamp(new java.util.Date().getTime());
    }


    public static String getPyIdxChar(String str) {
        String convert = "";
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            // 先判断其是否是汉字
            if(String.valueOf(word).matches("[\\u4E00-\\u9FA5]+")){
                //提取汉字的首字母
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
                if (pinyinArray != null) {
                    convert += pinyinArray[0].charAt(0);
                } else {
                    convert += word;
                }
            }
        }
        return convert.toUpperCase();
    }

    public static boolean isEmpty(String str) {
        if (str == null)
            return true;

        String c = str.trim();

        if (c.length() == 0)
            return true;

        return false;

    }

    public static boolean isEmpty(Object str) {
        if (str == null)
            return true;

        return false;

    }

    public static String toDateTimeStr(Timestamp time){
        if (null != time) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        } else {
            return "";
        }
    }

    public static String toDateStr(Timestamp time){
        if (null != time) {
            return new SimpleDateFormat("yyyy-MM-dd").format(time);
        } else {
            return "";
        }
    }

    public static Timestamp toDateTimestamp(String time){
        try {
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                    "yyyy-MM-dd");
            Date lFromDate1 = datetimeFormatter1.parse(time);
            System.out.println("gpsdate :" + lFromDate1);
            Timestamp fromTS1 = new Timestamp(lFromDate1.getTime());

            return fromTS1;

        } catch (Exception ex){
            return null;
        }
    }

    public static String intArr2Str(List<Integer> strList ){

        if(isEmpty(strList))
            return null;

        String newStr = strList.stream().map(String::valueOf).collect(Collectors.joining(","));

        return newStr;
    }

    public static List<Integer> intStr2Arr(String str){
        if(isEmpty(str))
            return  null;


        List<String> result = Arrays.asList(str.split(","));

        List<Integer> result2=new ArrayList<>();

        for (String t1: result  ) {
            result2.add(Integer.valueOf(t1));
        }

        return  result2;
    }
}
