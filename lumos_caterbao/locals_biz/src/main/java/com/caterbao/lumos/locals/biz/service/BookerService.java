package com.caterbao.lumos.locals.biz.service;


import com.caterbao.lumos.locals.biz.model.BookerBorrowBook;
import com.caterbao.lumos.locals.biz.model.BookerCalculateOverdueFineResult;
import com.caterbao.lumos.locals.common.FieldVo;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;

import java.sql.Timestamp;

public interface BookerService {
    BookerCalculateOverdueFineResult CalculateOverdueFine(String clientUserId);
    float CalculateOverdueFine(BookBorrow bookBorrow);
    FieldVo getBorrowWay(int way);
    FieldVo getReturnWay(int way);
    FieldVo getIdentityType(int type);
    FieldVo getBorrowStatus(int stauts, Timestamp expireTime);
    FieldVo getFlowType(int type);
}
