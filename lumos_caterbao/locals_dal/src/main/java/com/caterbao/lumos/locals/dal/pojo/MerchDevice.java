package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class MerchDevice {
    private String id;
    private String merchId;
    private String deviceId;
    private String cumCode;
    private String curStoreId;
    private String curShopId;
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

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCumCode() {
        return cumCode;
    }

    public void setCumCode(String cumCode) {
        this.cumCode = cumCode;
    }

    public String getCurStoreId() {
        return curStoreId;
    }

    public void setCurStoreId(String curStoreId) {
        this.curStoreId = curStoreId;
    }

    public String getCurShopId() {
        return curShopId;
    }

    public void setCurShopId(String curShopId) {
        this.curShopId = curShopId;
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
}
