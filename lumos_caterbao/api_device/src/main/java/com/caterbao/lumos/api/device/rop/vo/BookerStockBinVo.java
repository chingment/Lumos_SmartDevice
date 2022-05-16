package com.caterbao.lumos.api.device.rop.vo;

public class BookerStockBinVo {
    private String slotId;
    private String slotName;
    private int quantity;
    private String lastTakeStockTime;
    private boolean isOpen;

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLastTakeStockTime() {
        return lastTakeStockTime;
    }

    public void setLastTakeStockTime(String lastTakeStockTime) {
        this.lastTakeStockTime = lastTakeStockTime;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
