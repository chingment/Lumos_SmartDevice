package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.api.device.rop.vo.BookVo;

import java.util.List;

public class RetBookerBorrowReturn {
    private String flowId;
    private String actionCode;
    private List<BookVo> borrowBooks;
    private List<BookVo> returnBooks;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public List<BookVo> getBorrowBooks() {
        return borrowBooks;
    }

    public void setBorrowBooks(List<BookVo> borrowBooks) {
        this.borrowBooks = borrowBooks;
    }

    public List<BookVo> getReturnBooks() {
        return returnBooks;
    }

    public void setReturnBooks(List<BookVo> returnBooks) {
        this.returnBooks = returnBooks;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
}
