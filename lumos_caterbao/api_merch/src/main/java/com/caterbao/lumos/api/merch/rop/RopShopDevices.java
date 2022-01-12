package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

public class RopShopDevices extends BasePageRequest {
    private String storeId;
    private String shopId;
    private String deviceId;
    private String deviceCumCode;

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
}
