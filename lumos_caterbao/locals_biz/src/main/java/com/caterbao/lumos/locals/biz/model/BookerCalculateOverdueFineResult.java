package com.caterbao.lumos.locals.biz.model;

import java.util.List;

public class BookerCalculateOverdueFineResult {
    private float overdueFine;
    private List<BookerBorrowBook> borrowBooks;

    public float getOverdueFine() {
        return overdueFine;
    }

    public void setOverdueFine(float overdueFine) {
        this.overdueFine = overdueFine;
    }

    public List<BookerBorrowBook> getBorrowBooks() {
        return borrowBooks;
    }

    public void setBorrowBooks(List<BookerBorrowBook> borrowBooks) {
        this.borrowBooks = borrowBooks;
    }
}
