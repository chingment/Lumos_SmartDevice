package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.model.DeviceBean;

public class RetDeviceInitData {
    private DeviceBean device;
    private Object customData;

    public DeviceBean getDevice() {
        return device;
    }

    public void setDevice(DeviceBean device) {
        this.device = device;
    }

    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(Object customData) {
        this.customData = customData;
    }
}