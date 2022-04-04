package com.caterbao.lumos.locals.dal.pojo;

import java.sql.Timestamp;

public class Merch {
    private String id;
    private String name;
    private String merchUserId;
    private String borrowInstructions;
    private int userMaxBorrowQuantity;
    private int userMaxBorrowExpireDay;
    private int UserMaxBorrowRenewDay;
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

    public String getMerchUserId() {
        return merchUserId;
    }

    public void setMerchUserId(String merchUserId) {
        this.merchUserId = merchUserId;
    }

    public String getBorrowInstructions() {
        return borrowInstructions;
    }

    public void setBorrowInstructions(String borrowInstructions) {
        this.borrowInstructions = borrowInstructions;
    }

    public int getUserMaxBorrowQuantity() {
        return userMaxBorrowQuantity;
    }

    public void setUserMaxBorrowQuantity(int userMaxBorrowQuantity) {
        this.userMaxBorrowQuantity = userMaxBorrowQuantity;
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

    public int getUserMaxBorrowExpireDay() {
        return userMaxBorrowExpireDay;
    }

    public void setUserMaxBorrowExpireDay(int userMaxBorrowExpireDay) {
        this.userMaxBorrowExpireDay = userMaxBorrowExpireDay;
    }

    public int getUserMaxBorrowRenewDay() {
        return UserMaxBorrowRenewDay;
    }

    public void setUserMaxBorrowRenewDay(int userMaxBorrowRenewDay) {
        UserMaxBorrowRenewDay = userMaxBorrowRenewDay;
    }
}
