package com.caterbao.lumos.locals.biz.service;


import com.caterbao.lumos.locals.biz.model.BookerBorrowBookVo;
import com.caterbao.lumos.locals.biz.model.BookerCountBorrowBookResult;
import com.caterbao.lumos.locals.common.vo.FieldVo;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;

import java.sql.Timestamp;

public interface BookerService {
    BookerCountBorrowBookResult countBorrowBookResult(String clientUserId);
    BookerBorrowBookVo covertBorrowBookVo(BookBorrow bookBorrow);
    float calculateOverdueFine(Timestamp expireTime,int seq);
    FieldVo getBorrowWay(int way);
    FieldVo getReturnWay(int way);
    FieldVo getIdentityType(int type);
    FieldVo getBorrowStatus(int stauts, Timestamp expireTime);
    FieldVo getFlowType(int type);
    boolean chekIsWilldueBook(Timestamp expireTime);
    boolean chekIsOverdueBook(Timestamp expireTime);
    boolean checkCanRenew(Timestamp expireTime,int renewedCount,int maxRenewCount);
    boolean checkCanReturn(Timestamp expireTime,int seq,float skuPrice);
    boolean checkNeedPay(Timestamp expireTime,int seq,float skuPrice);
    String getIdentityName(int type,String id);
}
