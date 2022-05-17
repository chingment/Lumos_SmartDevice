package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

public class RopBookerStockSlots  extends BasePageRequest {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
