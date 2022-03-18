package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.vo.DeviceVo;

public class RetDeviceInitData {
    private DeviceVo device;
    private Object customData;

    public DeviceVo getDevice() {
        return device;
    }

    public void setDevice(DeviceVo device) {
        this.device = device;
    }

    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(Object customData) {
        this.customData = customData;
    }
}