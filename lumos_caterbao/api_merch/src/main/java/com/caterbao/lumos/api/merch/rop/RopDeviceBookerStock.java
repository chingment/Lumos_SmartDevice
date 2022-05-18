package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

public class RopDeviceBookerStock extends BasePageRequest {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
