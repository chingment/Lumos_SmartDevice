package com.caterbao.lumos.api.device.rop.vo;

public class BookerSlotVo {
    private String slotId;
    private String name;
    private BookerSlotDrivesVo drives;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public BookerSlotDrivesVo getDrives() {
        return drives;
    }

    public void setDrives(BookerSlotDrivesVo drives) {
        this.drives = drives;
    }
}
