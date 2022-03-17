package com.caterbao.lumos.locals.biz.service;


import com.caterbao.lumos.locals.biz.model.BookerBorrowBook;
import com.caterbao.lumos.locals.biz.model.BookerCalculateOverdueFineResult;
import com.caterbao.lumos.locals.common.FieldModel;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;

import java.sql.Timestamp;

public interface BookerService {
    BookerCalculateOverdueFineResult CalculateOverdueFine(String clientUserId);
    BookerBorrowBook CalculateOverdueFine(BookBorrow bookBorrow);
    FieldModel getBorrowWay(int way);
    FieldModel getReturnWay(int way);
    FieldModel getIdentityType(int type);
    FieldModel getBorrowStatus(int stauts, Timestamp expireTime);
    FieldModel getFlowType(int type);
}
