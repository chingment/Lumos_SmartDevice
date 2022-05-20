package com.caterbao.lumos.api.device.rop.vo;

import java.util.HashMap;

public class DeviceVo {
    private String deviceId;
    private String name;
    private String merchId;
    private String merchName;
    private int sceneMode;
    private int versionMode;
    private HashMap<String, DriveVo> drives;
    private MqttVo mqtt;
    private Boolean ExIsHas;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public int getSceneMode() {
        return sceneMode;
    }

    public void setSceneMode(int sceneMode) {
        this.sceneMode = sceneMode;
    }

    public int getVersionMode() {
        return versionMode;
    }

    public void setVersionMode(int versionMode) {
        this.versionMode = versionMode;
    }

    public HashMap<String, DriveVo> getDrives() {
        return drives;
    }

    public void setDrives(HashMap<String, DriveVo> drives) {
        this.drives = drives;
    }

    public MqttVo getMqtt() {
        return mqtt;
    }

    public void setMqtt(MqttVo mqtt) {
        this.mqtt = mqtt;
    }

    public Boolean getExIsHas() {
        return ExIsHas;
    }

    public void setExIsHas(Boolean exIsHas) {
        ExIsHas = exIsHas;
    }
}
