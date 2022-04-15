package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class AdSpace {
    private String id;
    private String name;
    private String supportFormat;
    private int supportMaxSize;
    private int vector;
    private String description;
    private String creator;
    private Timestamp createTime;
    private String mender;
    private Timestamp mendTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupportFormat() {
        return supportFormat;
    }

    public void setSupportFormat(String supportFormat) {
        this.supportFormat = supportFormat;
    }

    public int getVector() {
        return vector;
    }

    public void setVector(int vector) {
        this.vector = vector;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender;
    }

    public Timestamp getMendTime() {
        return mendTime;
    }

    public void setMendTime(Timestamp mendTime) {
        this.mendTime = mendTime;
    }

    public int getSupportMaxSize() {
        return supportMaxSize;
    }

    public void setSupportMaxSize(int supportMaxSize) {
        this.supportMaxSize = supportMaxSize;
    }
}
