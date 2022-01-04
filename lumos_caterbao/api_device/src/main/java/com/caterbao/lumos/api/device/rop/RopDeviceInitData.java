package com.caterbao.lumos.api.device.rop;

public class RopDeviceInitData {
    public String deviceId;
    public String imeiId;
    public String macAddr;
    public String sceneMode;
    public String versionMode;
    public String appVerCode;
    public String appVerName;
    public String sysVerName;
    public String ctrlVerName;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImeiId() {
        return imeiId;
    }

    public void setImeiId(String imeiId) {
        this.imeiId = imeiId;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getAppVerCode() {
        return appVerCode;
    }

    public void setAppVerCode(String appVerCode) {
        this.appVerCode = appVerCode;
    }

    public String getAppVerName() {
        return appVerName;
    }

    public void setAppVerName(String appVerName) {
        this.appVerName = appVerName;
    }

    public String getSceneMode() {
        return sceneMode;
    }

    public void setSceneMode(String sceneMode) {
        this.sceneMode = sceneMode;
    }

    public String getVersionMode() {
        return versionMode;
    }

    public void setVersionMode(String versionMode) {
        this.versionMode = versionMode;
    }

    public String getSysVerName() {
        return sysVerName;
    }

    public void setSysVerName(String sysVerName) {
        this.sysVerName = sysVerName;
    }

    public String getCtrlVerName() {
        return ctrlVerName;
    }

    public void setCtrlVerName(String ctrlVerName) {
        this.ctrlVerName = ctrlVerName;
    }
}
