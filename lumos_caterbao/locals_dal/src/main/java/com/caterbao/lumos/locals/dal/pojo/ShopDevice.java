package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class ShopDevice {

    private String id;
    private String merchId;
    private String storeId;
    private String shopId;
    private String deviceId;
    private int bindStatus;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(int bindStatus) {
        this.bindStatus = bindStatus;
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
