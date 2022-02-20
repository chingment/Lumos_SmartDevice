package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class BookBorrowReturnFlow {
    private String id;
    private String merchId;
    private String storeId;
    private String shopId;
    private String deviceId;
    private String cabinetId;
    private String slotId;
    private String clientUserId;
    private int identityType;
    private String identityId;
    private int openActionResult;
    private String openActionCode;
    private Timestamp openActionTime;
    private String openRfIds;
    private int closeActionResult;
    private String closeActionCode;
    private Timestamp closeActionTime;
    private String closeRfIds;
    private int status; //1 等待打开柜门 2 打开成功 3 打开失败 4 等待关闭门 5 关闭成功 6 关闭失败;
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

    public int getOpenActionResult() {
        return openActionResult;
    }

    public void setOpenActionResult(int openActionResult) {
        this.openActionResult = openActionResult;
    }

    public String getOpenActionCode() {
        return openActionCode;
    }

    public void setOpenActionCode(String openActionCode) {
        this.openActionCode = openActionCode;
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

    public int getCloseActionResult() {
        return closeActionResult;
    }

    public void setCloseActionResult(int closeActionResult) {
        this.closeActionResult = closeActionResult;
    }

    public String getCloseActionCode() {
        return closeActionCode;
    }

    public void setCloseActionCode(String closeActionCode) {
        this.closeActionCode = closeActionCode;
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
}
