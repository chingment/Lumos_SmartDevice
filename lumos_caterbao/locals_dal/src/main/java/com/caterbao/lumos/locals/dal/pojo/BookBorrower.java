package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class BookBorrower {
    private String id;
    private String name;
    private String borrowCardNo;
    private String borrowCardPwd;
    private int identityType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorrowCardNo() {
        return borrowCardNo;
    }

    public void setBorrowCardNo(String borrowCardNo) {
        this.borrowCardNo = borrowCardNo;
    }

    public String getBorrowCardPwd() {
        return borrowCardPwd;
    }

    public void setBorrowCardPwd(String borrowCardPwd) {
        this.borrowCardPwd = borrowCardPwd;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
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
