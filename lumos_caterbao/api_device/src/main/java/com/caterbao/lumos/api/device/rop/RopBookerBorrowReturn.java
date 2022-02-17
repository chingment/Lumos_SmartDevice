package com.caterbao.lumos.api.device.rop;

public class RopBookerBorrowReturn {
    private String deviceId;
    private String cabientId;
    private String slotId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCabientId() {
        return cabientId;
    }

    public void setCabientId(String cabientId) {
        this.cabientId = cabientId;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }
}
