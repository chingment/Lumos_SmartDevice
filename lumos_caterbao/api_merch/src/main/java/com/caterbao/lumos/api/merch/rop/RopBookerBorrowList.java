package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

import java.util.List;

public class RopBookerBorrowList extends BasePageRequest {

    private String deviceCode;
    private String flowId;
    private String skuName;
    private List<String> borrowTimeArea;
    private List<String> returnTimeArea;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public List<String> getBorrowTimeArea() {
        return borrowTimeArea;
    }

    public void setBorrowTimeArea(List<String> borrowTimeArea) {
        this.borrowTimeArea = borrowTimeArea;
    }

    public List<String> getReturnTimeArea() {
        return returnTimeArea;
    }

    public void setReturnTimeArea(List<String> returnTimeArea) {
        this.returnTimeArea = returnTimeArea;
    }
}
