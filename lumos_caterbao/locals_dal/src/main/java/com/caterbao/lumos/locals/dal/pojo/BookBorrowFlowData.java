package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class BookBorrowFlowData {
    private String id;
    private String flowId;
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
    private String clientUserId;
    private int identityType;
    private String identityId;
    private String identityName;
    private String skuId;
    private String skuName;
    private String skuImgUrl;
    private String skuCumCode;
    private String skuRfId;
    private int borrowSeq;
    private int borrowWay;
    private Timestamp borrowTime;
    private int borrowStatus;
    private int returnWay;
    private Timestamp returnTime;
    private String returnFlowId;
    private Timestamp expireTime;
    private Timestamp renewLastTime;
    private int renewCount;
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

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
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

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuImgUrl() {
        return skuImgUrl;
    }

    public void setSkuImgUrl(String skuImgUrl) {
        this.skuImgUrl = skuImgUrl;
    }

    public String getSkuCumCode() {
        return skuCumCode;
    }

    public void setSkuCumCode(String skuCumCode) {
        this.skuCumCode = skuCumCode;
    }

    public String getSkuRfId() {
        return skuRfId;
    }

    public void setSkuRfId(String skuRfId) {
        this.skuRfId = skuRfId;
    }

    public int getBorrowSeq() {
        return borrowSeq;
    }

    public void setBorrowSeq(int borrowSeq) {
        this.borrowSeq = borrowSeq;
    }

    public Timestamp getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Timestamp borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Timestamp getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Timestamp returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnFlowId() {
        return returnFlowId;
    }

    public void setReturnFlowId(String returnFlowId) {
        this.returnFlowId = returnFlowId;
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

    public int getBorrowWay() {
        return borrowWay;
    }

    public void setBorrowWay(int borrowWay) {
        this.borrowWay = borrowWay;
    }

    public int getReturnWay() {
        return returnWay;
    }

    public void setReturnWay(int returnWay) {
        this.returnWay = returnWay;
    }

    public int getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(int borrowStatus) {
        this.borrowStatus = borrowStatus;
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

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    public Timestamp getRenewLastTime() {
        return renewLastTime;
    }

    public void setRenewLastTime(Timestamp renewLastTime) {
        this.renewLastTime = renewLastTime;
    }

    public int getRenewCount() {
        return renewCount;
    }

    public void setRenewCount(int renewCount) {
        this.renewCount = renewCount;
    }
}
