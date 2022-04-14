package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.vo.RfTagVo;

import java.util.HashMap;
import java.util.List;

public class RopBookerTakeStock {
    private String deviceId;
    private List<RfTagVo> rfTags;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<RfTagVo> getRfTags() {
        return rfTags;
    }

    public void setRfTags(List<RfTagVo> rfTags) {
        this.rfTags = rfTags;
    }
}
