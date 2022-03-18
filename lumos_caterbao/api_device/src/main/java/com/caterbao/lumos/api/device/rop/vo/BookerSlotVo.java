package com.caterbao.lumos.api.device.rop.vo;

public class BookerSlotVo {
    private String slotId;
    private String name;
    private BookerSlotDriveVo drive;

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BookerSlotDriveVo getDrive() {
        return drive;
    }

    public void setDrive(BookerSlotDriveVo drive) {
        this.drive = drive;
    }
}
