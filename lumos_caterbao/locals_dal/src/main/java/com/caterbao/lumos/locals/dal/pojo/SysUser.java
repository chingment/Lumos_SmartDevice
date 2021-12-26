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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSecurityStamp() {
        return SecurityStamp;
    }

    public void setSecurityStamp(String securityStamp) {
        SecurityStamp = securityStamp;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public Timestamp getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }

    public String getMender() {
        return Mender;
    }

    public void setMender(String mender) {
        Mender = mender;
    }

    public Timestamp getMendTime() {
        return MendTime;
    }

    public void setMendTime(Timestamp mendTime) {
        MendTime = mendTime;
    }
}
