package com.caterbao.lumos.locals.dal;

import java.util.UUID;

public class IdWork {

    public static final String generateGUID()
    {
        UUID uuid= UUID.randomUUID();
        return uuid.toString();
    }
}
