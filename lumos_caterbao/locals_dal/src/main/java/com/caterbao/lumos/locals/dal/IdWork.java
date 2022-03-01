package com.caterbao.lumos.locals.dal;

import java.util.UUID;

public class IdWork {

    public static final String buildGuId() {
        String uuid = UUID.randomUUID().toString().toString().replace("-", "");
        return uuid;
    }

    public static final String buildLongId() {
        return String.valueOf(SnowFlake.nextId());
    }



//    public static final String generateId() {
//        long id = SnowFlake.nextId();
//        //String uuid = UUID.randomUUID().toString().toString().replace("-", "");
//        return String.valueOf(id);
//    }
}
