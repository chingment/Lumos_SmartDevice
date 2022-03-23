package com.caterbao.lumos.locals.biz.service;


import com.caterbao.lumos.locals.biz.model.BookerCountBorrowBookResult;
import com.caterbao.lumos.locals.common.vo.FieldVo;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;

import java.sql.Timestamp;

public interface BookerService {
    BookerCountBorrowBookResult CountBorrowBookResult(String clientUserId);
    float CalculateOverdueFine(Timestamp expireTime,int seq);
    FieldVo getBorrowWay(int way);
    FieldVo getReturnWay(int way);
    FieldVo getIdentityType(int type);
    FieldVo getBorrowStatus(int stauts, Timestamp expireTime);
    FieldVo getFlowType(int type);
    boolean chekIsWilldueBook(Timestamp expireTime);
    boolean chekIsOverdueBook(Timestamp expireTime);
    boolean checkCanRenew(int renewedCount,int maxRenewCount);
    boolean checkCanReturn(Timestamp expireTime,int seq,float skuPrice);
    boolean checkNeedPay(Timestamp expireTime,int seq,float skuPrice);
}
