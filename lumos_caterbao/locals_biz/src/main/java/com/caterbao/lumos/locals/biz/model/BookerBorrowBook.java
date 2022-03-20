package com.caterbao.lumos.locals.biz.model;

import com.caterbao.lumos.locals.common.FieldVo;

public class BookerBorrowBook {
    private String borrowId;
    private String skuId;
    private String skuImgUrl;
    private String skuName;
    private String skuCumCode;
    private String skuRfId;
    private String borrowTime;
    private String returnTime;
    private String expireTime;
    private float overdueFine;
    private FieldVo borrowWay;
    private FieldVo status;
    private String renewLastTime;
    private int renewCount;

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuImgUrl() {
        return skuImgUrl;
    }

    public void setSkuImgUrl(String skuImgUrl) {
        this.skuImgUrl = skuImgUrl;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public float getOverdueFine() {
        return overdueFine;
    }

    public void setOverdueFine(float overdueFine) {
        this.overdueFine = overdueFine;
    }

    public FieldVo getStatus() {
        return status;
    }

    public void setStatus(FieldVo status) {
        this.status = status;
    }

    public FieldVo getBorrowWay() {
        return borrowWay;
    }

    public void setBorrowWay(FieldVo borrowWay) {
        this.borrowWay = borrowWay;
    }

    public String getRenewLastTime() {
        return renewLastTime;
    }

    public void setRenewLastTime(String renewLastTime) {
        this.renewLastTime = renewLastTime;
    }

    public int getRenewCount() {
        return renewCount;
    }

    public void setRenewCount(int renewCount) {
        this.renewCount = renewCount;
    }
}
