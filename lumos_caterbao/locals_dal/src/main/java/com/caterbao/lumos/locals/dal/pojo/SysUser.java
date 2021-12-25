package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class SysUser {
    private String id;
    private String userName;
    private String passwordHash;
    private String SecurityStamp;
    private String Creator;
    private Timestamp CreateTime;
    private String Mender;
    private Timestamp MendTime;
}
