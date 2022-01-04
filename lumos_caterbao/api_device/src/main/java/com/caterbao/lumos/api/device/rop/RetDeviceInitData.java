package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.model.DeviceBean;

public class RetDeviceInitData {
     private DeviceBean deivce;
     private Object cumstonData;

    public DeviceBean getDeivce() {
        return deivce;
    }

    public void setDeivce(DeviceBean deivce) {
        this.deivce = deivce;
    }

    public Object getCumstonData() {
        return cumstonData;
    }

    public void setCumstonData(Object cumstonData) {
        this.cumstonData = cumstonData;
    }
}
