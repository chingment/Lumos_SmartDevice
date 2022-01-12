package com.caterbao.lumos.locals.common;


import net.sourceforge.pinyin4j.PinyinHelper;

import java.sql.Timestamp;

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

}
