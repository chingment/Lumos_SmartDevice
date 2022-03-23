package com.caterbao.lumos.locals.biz.model;

import java.util.List;

public class BookerCountBorrowBookResult {
    private float overdueFine;
    private int borrowedQuantity;
    private int willdueQuantity;
    private int overdueQuantity;

    public float getOverdueFine() {
        return overdueFine;
    }

    public void setOverdueFine(float overdueFine) {
        this.overdueFine = overdueFine;
    }

    public int getBorrowedQuantity() {
        return borrowedQuantity;
    }

    public void setBorrowedQuantity(int borrowedQuantity) {
        this.borrowedQuantity = borrowedQuantity;
    }

    public int getWilldueQuantity() {
        return willdueQuantity;
    }

    public void setWilldueQuantity(int willdueQuantity) {
        this.willdueQuantity = willdueQuantity;
    }

    public int getOverdueQuantity() {
        return overdueQuantity;
    }

    public void setOverdueQuantity(int overdueQuantity) {
        this.overdueQuantity = overdueQuantity;
    }
}
