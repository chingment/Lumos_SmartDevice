package com.caterbao.lumos.locals.biz.service.impl;

import com.caterbao.lumos.locals.biz.model.BookerBorrowBook;
import com.caterbao.lumos.locals.biz.model.BookerCalculateOverdueFineResult;
import com.caterbao.lumos.locals.biz.service.BookerService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.FieldModel;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowReturnFlowDataMapper;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowReturnFlowData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("BizBookerService")
public class BookerServiceImpl implements BookerService {

    private BookBorrowReturnFlowDataMapper bookBorrowReturnFlowDataMapper;
    @Autowired(required = false)
    public void setBookBorrowReturnFlowDataMapper(BookBorrowReturnFlowDataMapper bookBorrowReturnFlowDataMapper) {
        this.bookBorrowReturnFlowDataMapper = bookBorrowReturnFlowDataMapper;
    }
    @Override
    public BookerCalculateOverdueFineResult CalculateOverdueFine(String clientUserId) {

        BookerCalculateOverdueFineResult result = new BookerCalculateOverdueFineResult();

        LumosSelective selective_1 = new LumosSelective();
        selective_1.setFields("*");
        selective_1.addWhere("ClientUserId", clientUserId);

        List<BookBorrowReturnFlowData> d_BookBorrowReturnFlowDatas = bookBorrowReturnFlowDataMapper.find(selective_1);


        List<BookerBorrowBook> bookerBorrowBooks = new ArrayList<>();
        float sumSverdueFine=0;
        for (int i = 0; i < d_BookBorrowReturnFlowDatas.size(); i++) {
            BookBorrowReturnFlowData d_BookBorrowReturnFlowData = d_BookBorrowReturnFlowDatas.get(i);
            BookerBorrowBook bookerBorrowBook = CalculateOverdueFine(d_BookBorrowReturnFlowData);
            sumSverdueFine += bookerBorrowBook.getOverdueFine();
            bookerBorrowBooks.add(bookerBorrowBook);
        }

        result.setBorrowBooks(bookerBorrowBooks);
        result.setOverdueFine(sumSverdueFine);
        return result;
    }

    @Override
    public BookerBorrowBook CalculateOverdueFine(BookBorrowReturnFlowData bookBorrowReturnFlowData) {
        BookerBorrowBook bookerBorrowBook = new BookerBorrowBook();

        bookerBorrowBook.setSkuId(bookBorrowReturnFlowData.getSkuId());
        bookerBorrowBook.setRfId(bookBorrowReturnFlowData.getSkuRfId());
        bookerBorrowBook.setName(bookBorrowReturnFlowData.getSkuName());
        bookerBorrowBook.setCumCode(bookBorrowReturnFlowData.getSkuCumCode());
        bookerBorrowBook.setImgUrl(bookBorrowReturnFlowData.getSkuImgUrl());
        bookerBorrowBook.setBorrowTime(CommonUtil.toDateTimeStr(bookBorrowReturnFlowData.getBorrowTime()));

        long l = CommonUtil.getDateTimeNow().getTime() - bookBorrowReturnFlowData.getBorrowTime().getTime();
        long diffDay = l / (24 * 60 * 60 * 1000);

        float overdueFine = 0;

        FieldModel status = new FieldModel();
        if (diffDay <= 3) {
            overdueFine = 0;
            status = new FieldModel(1, "借阅中");
        } else if (diffDay > 3 && diffDay <= 30) {
            status = new FieldModel(2, "逾期借阅");
            if (bookBorrowReturnFlowData.getBorrowSeq() <= 2) {
                overdueFine = (diffDay - 3) * 0.5f;
            } else {
                overdueFine = (diffDay - 3) * 1f;
            }
        } else if (diffDay > 30) {
            status = new FieldModel(3, "逾期借阅");
            overdueFine = 40f;
        }

        bookerBorrowBook.setStatus(status);
        bookerBorrowBook.setOverdueFine(overdueFine);

        return bookerBorrowBook;
    }
}
