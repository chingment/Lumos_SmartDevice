package com.caterbao.lumos.api.device.rop.vo;

public class CabinetVo {
    private String cabinetId;

    private String name;
    private String comId;
    private int comBaud;
    private String comPrl;
    private String layout;
    private int priority;

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
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

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
