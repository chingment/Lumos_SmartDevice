package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.ImgVo;

import java.util.List;

public class RopAdminUserAdd {
    private String userName;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private List<ImgVo> avatar;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public List<ImgVo> getAvatar() {
        return avatar;
    }

    public void setAvatar(List<ImgVo> avatar) {
        this.avatar = avatar;
    }
}
