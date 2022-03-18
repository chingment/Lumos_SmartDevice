package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class DeviceDrive {
    private String id;
    private String deviceId;
    private String driveId;
    private String name;
    private String comId;
    private int comBaud;
    private String comPrl;

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

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public int getComBaud() {
        return comBaud;
    }

    public void setComBaud(int comBaud) {
        this.comBaud = comBaud;
    }

    public String getComPrl() {
        return comPrl;
    }

    public void setComPrl(String comPrl) {
        this.comPrl = comPrl;
    }
}
