package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class BookFlow {
    private String id;
    private String merchId;
    private String merchName;
    private String storeId;
    private String storeName;
    private String shopId;
    private String shopName;
    private String deviceId;
    private String deviceCumCode;
    private String cabinetId;
    private String slotId;
    private int type;
    private String clientUserId;
    private int identityType;
    private String identityId;
    private String identityName;
    private Timestamp openActionTime;
    private String openRfIds;
    private Timestamp closeActionTime;
    private String closeRfIds;
    private int status;
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

    public String getDeviceCumCode() {
        return deviceCumCode;
    }

    public void setDeviceCumCode(String deviceCumCode) {
        this.deviceCumCode = deviceCumCode;
    }

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public Timestamp getOpenActionTime() {
        return openActionTime;
    }

    public void setOpenActionTime(Timestamp openActionTime) {
        this.openActionTime = openActionTime;
    }

    public String getOpenRfIds() {
        return openRfIds;
    }

    public void setOpenRfIds(String openRfIds) {
        this.openRfIds = openRfIds;
    }

    public Timestamp getCloseActionTime() {
        return closeActionTime;
    }

    public void setCloseActionTime(Timestamp closeActionTime) {
        this.closeActionTime = closeActionTime;
    }

    public String getCloseRfIds() {
        return closeRfIds;
    }

    public void setCloseRfIds(String closeRfIds) {
        this.closeRfIds = closeRfIds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
