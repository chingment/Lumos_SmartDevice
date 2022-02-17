package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.model.BookBean;

import java.util.List;

public class RetBookerBorrowReturn {
    private List<BookBean> borrowBooks;
    private List<BookBean> returnBooks;

    public List<BookBean> getBorrowBooks() {
        return borrowBooks;
    }

    public void setBorrowBooks(List<BookBean> borrowBooks) {
        this.borrowBooks = borrowBooks;
    }

    public List<BookBean> getReturnBooks() {
        return returnBooks;
    }

    public void setReturnBooks(List<BookBean> returnBooks) {
        this.returnBooks = returnBooks;
    }
}
