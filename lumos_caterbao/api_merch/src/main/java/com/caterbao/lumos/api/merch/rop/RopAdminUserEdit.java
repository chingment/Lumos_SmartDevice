package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.FileVo;

import java.util.List;

public class RopAdminUserEdit {
    private String id;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private List<FileVo> avatar;
    private Boolean isDisable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FileVo> getAvatar() {
        return avatar;
    }

    public void setAvatar(List<FileVo> avatar) {
        this.avatar = avatar;
    }

    public Boolean getDisable() {
        return isDisable;
    }

    public Boolean getIsDisable() {
        return isDisable;
    }

    public void setDisable(Boolean isDisable) {
        isDisable = isDisable;
    }
}
