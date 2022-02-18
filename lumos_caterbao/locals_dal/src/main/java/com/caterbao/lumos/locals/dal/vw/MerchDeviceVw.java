package com.caterbao.lumos.locals.dal.vw;

import java.sql.Timestamp;

public class MerchDeviceVw {
    private String id;
    private String cumCode;
    private String imgUrl;
    private int bindStatus;
    private int sceneMode;
    private int versionMode;
    private String merchId;
    private String ShopId;
    private String ShopName;
    private String storeId;
    private String storeName;
    private String imeiId;
    private String model;
    private String macAddr;
    private String appVerName;
    private String sysVerName;
    private String ctrlVerName;
    private int runMode;
    private Timestamp lastRunTime;
    private int lastRunStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCumCode() {
        return cumCode;
    }

    public void setCumCode(String cumCode) {
        this.cumCode = cumCode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(int bindStatus) {
        this.bindStatus = bindStatus;
    }

    public int getSceneMode() {
        return sceneMode;
    }

    public void setSceneMode(int sceneMode) {
        this.sceneMode = sceneMode;
    }

    public int getVersionMode() {
        return versionMode;
    }

    public void setVersionMode(int versionMode) {
        this.versionMode = versionMode;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getImeiId() {
        return imeiId;
    }

    public void setImeiId(String imeiId) {
        this.imeiId = imeiId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getAppVerName() {
        return appVerName;
    }

    public void setAppVerName(String appVerName) {
        this.appVerName = appVerName;
    }

    public String getSysVerName() {
        return sysVerName;
    }

    public void setSysVerName(String sysVerName) {
        this.sysVerName = sysVerName;
    }

    public String getCtrlVerName() {
        return ctrlVerName;
    }

    public void setCtrlVerName(String ctrlVerName) {
        this.ctrlVerName = ctrlVerName;
    }

    public int getRunMode() {
        return runMode;
    }

    public void setRunMode(int runMode) {
        this.runMode = runMode;
    }

    public Timestamp getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Timestamp lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public int getLastRunStatus() {
        return lastRunStatus;
    }

    public void setLastRunStatus(int lastRunStatus) {
        this.lastRunStatus = lastRunStatus;
    }
}
