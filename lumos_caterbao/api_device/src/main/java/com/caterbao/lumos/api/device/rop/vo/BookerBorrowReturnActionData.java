package com.caterbao.lumos.api.device.rop.vo;

import java.util.List;

public class BookerBorrowReturnActionData {
    private List<String> openRfIds;
    private List<String> closeRfIds;

    public List<String> getOpenRfIds() {
        return openRfIds;
    }

    public void setOpenRfIds(List<String> openRfIds) {
        this.openRfIds = openRfIds;
    }

    public List<String> getCloseRfIds() {
        return closeRfIds;
    }

    public void setCloseRfIds(List<String> closeRfIds) {
        this.closeRfIds = closeRfIds;
    }
}
