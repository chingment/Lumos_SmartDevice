package com.caterbao.lumos.locals.biz.service.impl;

import com.caterbao.lumos.locals.biz.model.BookerBorrowBook;
import com.caterbao.lumos.locals.biz.model.BookerCalculateOverdueFineResult;
import com.caterbao.lumos.locals.biz.service.BookerService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.FieldModel;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowMapper;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service("BizBookerService")
public class BookerServiceImpl implements BookerService {

    private BookBorrowMapper bookBorrowMapper;
    @Autowired(required = false)
    public void setBookBorrowMapper(BookBorrowMapper bookBorrowMapper) {
        this.bookBorrowMapper = bookBorrowMapper;
    }
    @Override
    public BookerCalculateOverdueFineResult CalculateOverdueFine(String clientUserId) {

        BookerCalculateOverdueFineResult result = new BookerCalculateOverdueFineResult();

        LumosSelective selective_1 = new LumosSelective();
        selective_1.setFields("*");
        selective_1.addWhere("ClientUserId", clientUserId);

        List<BookBorrow> d_BookBorrows = bookBorrowMapper.find(selective_1);


        List<BookerBorrowBook> bookerBorrowBooks = new ArrayList<>();
        float sumSverdueFine=0;
        for (int i = 0; i < d_BookBorrows.size(); i++) {
            BookBorrow d_BookBorrow = d_BookBorrows.get(i);
            BookerBorrowBook bookerBorrowBook = CalculateOverdueFine(d_BookBorrow);
            sumSverdueFine += bookerBorrowBook.getOverdueFine();
            bookerBorrowBooks.add(bookerBorrowBook);
        }

        result.setBorrowBooks(bookerBorrowBooks);
        result.setOverdueFine(sumSverdueFine);
        return result;
    }

    @Override
    public BookerBorrowBook CalculateOverdueFine(BookBorrow bookBorrow) {
        BookerBorrowBook bookerBorrowBook = new BookerBorrowBook();

        bookerBorrowBook.setSkuId(bookBorrow.getSkuId());
        bookerBorrowBook.setRfId(bookBorrow.getSkuRfId());
        bookerBorrowBook.setName(bookBorrow.getSkuName());
        bookerBorrowBook.setCumCode(bookBorrow.getSkuCumCode());
        bookerBorrowBook.setImgUrl(bookBorrow.getSkuImgUrl());
        bookerBorrowBook.setBorrowTime(CommonUtil.toDateTimeStr(bookBorrow.getBorrowTime()));

        long l = CommonUtil.getDateTimeNow().getTime() - bookBorrow.getBorrowTime().getTime();
        long diffDay = l / (24 * 60 * 60 * 1000);

        float overdueFine = 0;

        FieldModel status = new FieldModel();
        if (diffDay <= 3) {
            overdueFine = 0;
            status = new FieldModel(1, "借阅中");
        } else if (diffDay > 3 && diffDay <= 30) {
            status = new FieldModel(2, "逾期借阅");
            if (bookBorrow.getBorrowSeq() <= 2) {
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

    @Override
    public FieldModel getBorrowStatus(int stauts,Timestamp expireTime) {
        //todo 未判断超时
        FieldModel model = new FieldModel();
        if (stauts == 1000) {
            return new FieldModel(1000, "借阅中");
        } else if (stauts == 2000) {
            return new FieldModel(2000, "已超期");
        } else if (stauts == 3000)
            return new FieldModel(3000, "已归还");
        else if (stauts == 4000)
            return new FieldModel(4000, "已购买");
        return model;
    }

    @Override
    public FieldModel getReturnWay(int way) {
        FieldModel model=new FieldModel();
        if(way==1)
            return new FieldModel(1,"自助设备");
        return model;
    }

    @Override
    public FieldModel getIdentityType(int type) {
        FieldModel model = new FieldModel();
        if (type == 1)
            return new FieldModel(1, "IC卡");
        else if (type == 2)
            return new FieldModel(2, "小程序");
        return model;
    }

    @Override
    public FieldModel getBorrowWay(int way) {
        FieldModel model=new FieldModel();
        if(way==1)
            return new FieldModel(1,"自助设备");
        return model;
    }

    @Override
    public FieldModel getFlowType(int type) {
        FieldModel model=new FieldModel();
        if(type==1)
            return new FieldModel(1,"借还");
        return model;
    }
}
