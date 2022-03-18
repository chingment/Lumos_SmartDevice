package com.caterbao.lumos.api.device.rop.vo;

public class BookerSlotDriveVo {
    private BookerDriveLockeqVo lockeq;
    private BookerDriveRfeqVo rfeq;
    public BookerDriveLockeqVo getLockeq() {
        return lockeq;
    }
    public void setLockeq(BookerDriveLockeqVo lockeq) {
        this.lockeq = lockeq;
    }
    public BookerDriveRfeqVo getRfeq() {
        return rfeq;
    }
    public void setRfeq(BookerDriveRfeqVo rfeq) {
        this.rfeq = rfeq;
    }
}
