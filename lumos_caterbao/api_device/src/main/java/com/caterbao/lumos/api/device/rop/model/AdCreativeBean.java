package com.caterbao.lumos.api.device.rop.model;

import java.io.Serializable;

public class AdCreatviceBean implements Serializable {

    private String dataType;
    private String dataUrl;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }
}
