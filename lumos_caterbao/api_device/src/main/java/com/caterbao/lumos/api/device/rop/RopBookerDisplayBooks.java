package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

public class RopBookerDisplayBooks extends BasePageRequest {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
