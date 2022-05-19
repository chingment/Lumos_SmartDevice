package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

import java.util.List;

public class RopBookerFlowList extends BasePageRequest {
    private String flowId;
    private String deviceCode;
    private String actionCode;
    private List<String> createTimeArea;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public List<String> getCreateTimeArea() {
        return createTimeArea;
    }

    public void setCreateTimeArea(List<String> createTimeArea) {
        this.createTimeArea = createTimeArea;
    }
}
