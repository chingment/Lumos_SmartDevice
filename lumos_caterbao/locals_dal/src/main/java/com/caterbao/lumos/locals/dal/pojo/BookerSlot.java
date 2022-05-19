package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class BookerSlot {
    private String id;
    private String deviceId;
    private String slotId;
    private String name;
    private String lockeqId;
    private String lockeqAnt;
    private String rfeqId;
    private String rfeqAnt;
    private Timestamp lastInboundTime;
    private String lastInboundSheetId;
    private String creator;
    private Timestamp createTime;
    private String mender;
    private Timestamp mendTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

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

    public String getLockeqId() {
        return lockeqId;
    }

    public void setLockeqId(String lockeqId) {
        this.lockeqId = lockeqId;
    }

    public String getLockeqAnt() {
        return lockeqAnt;
    }

    public void setLockeqAnt(String lockeqAnt) {
        this.lockeqAnt = lockeqAnt;
    }

    public String getRfeqId() {
        return rfeqId;
    }

    public void setRfeqId(String rfeqId) {
        this.rfeqId = rfeqId;
    }

    public String getRfeqAnt() {
        return rfeqAnt;
    }

    public void setRfeqAnt(String rfeqAnt) {
        this.rfeqAnt = rfeqAnt;
    }

    public Timestamp getLastInboundTime() {
        return lastInboundTime;
    }

    public void setLastInboundTime(Timestamp lastInboundTime) {
        this.lastInboundTime = lastInboundTime;
    }

    public String getLastInboundSheetId() {
        return lastInboundSheetId;
    }

    public void setLastInboundSheetId(String lastInboundSheetId) {
        this.lastInboundSheetId = lastInboundSheetId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender;
    }

    public Timestamp getMendTime() {
        return mendTime;
    }

    public void setMendTime(Timestamp mendTime) {
        this.mendTime = mendTime;
    }
}
