package com.caterbao.lumos.locals.common;


import java.sql.Timestamp;

public class CommonUtil {
    public  static String HelloWorld(){

        System.out.print("HelloWorld");
        return "HelloWorld";
    }

    public static Timestamp getDateTimeNow(){
        return  new Timestamp(new java.util.Date().getTime());
    }
}
