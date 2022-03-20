package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

public class RopBookerSawBorrowBooks  extends BasePageRequest {
    private String deviceId;
    private String clientUserId;
    private int identityType;
    private String identityId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
}
