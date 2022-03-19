package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.FileVo;

import java.util.List;

public class RopBorrowerAdd {
    private String cardNo;
    private String cardPwd;
    private String fullName;
    private String phoneNumber;
    private String email;
    private List<FileVo> avatar;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardPwd() {
        return cardPwd;
    }

    public void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
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
}
