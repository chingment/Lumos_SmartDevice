package com.caterbao.lumos.locals.common;

import java.util.UUID;

public class TraceLogUtils {
    public static String getTraceId(){

        String guid = UUID.randomUUID().toString().replace("-", "");
        return guid;
    }
}
