package com.caterbao.lumos.locals.dal;

import java.util.UUID;

public class IdWork {

    public static final String generateGUID() {
        String uuid = UUID.randomUUID().toString().toString().replace("-", "");
        return uuid;
    }

//    public static final String generateId() {
//        long id = SnowFlake.nextId();
//        //String uuid = UUID.randomUUID().toString().toString().replace("-", "");
//        return String.valueOf(id);
//    }
}
