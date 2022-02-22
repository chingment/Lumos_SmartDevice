package com.caterbao.lumos.locals.biz.service;


import com.caterbao.lumos.locals.biz.model.BookerBorrowBook;
import com.caterbao.lumos.locals.biz.model.BookerCalculateOverdueFineResult;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowReturnFlowData;

public interface BookerService {
    BookerCalculateOverdueFineResult CalculateOverdueFine(String clientUserId);
    BookerBorrowBook CalculateOverdueFine(int num, BookBorrowReturnFlowData bookBorrowReturnFlowData);
}
