package com.caterbao.lumos.api.device.rop;

import java.io.Serializable;

public class RopOwnLogout  implements Serializable {
    private String userId;
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
