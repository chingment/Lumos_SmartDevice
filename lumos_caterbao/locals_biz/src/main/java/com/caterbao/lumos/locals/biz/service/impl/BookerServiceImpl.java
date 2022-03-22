package com.caterbao.lumos.locals.biz.service.impl;

import com.caterbao.lumos.locals.biz.model.BookerBorrowBook;
import com.caterbao.lumos.locals.biz.model.BookerCalculateOverdueFineResult;
import com.caterbao.lumos.locals.biz.service.BookerService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.vo.FieldVo;
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
        float sumOverdueFine=0;
        for (int i = 0; i < d_BookBorrows.size(); i++) {
            BookBorrow d_BookBorrow = d_BookBorrows.get(i);

            BookerBorrowBook m_BookerBorrowBook=new BookerBorrowBook();
            m_BookerBorrowBook.setBorrowId(d_BookBorrow.getId());
            float overdueFine = CalculateOverdueFine(d_BookBorrow);
            sumOverdueFine += overdueFine;
            bookerBorrowBooks.add(m_BookerBorrowBook);
        }

        result.setBorrowBooks(bookerBorrowBooks);
        result.setOverdueFine(sumOverdueFine);
        return result;
    }

    @Override
    public float CalculateOverdueFine(BookBorrow bookBorrow) {

        long l = CommonUtil.getDateTimeNow().getTime() - bookBorrow.getBorrowTime().getTime();
        long diffDay = l / (24 * 60 * 60 * 1000);

        float overdueFine = 0;

        if (diffDay <= 3) {
            overdueFine = 0;
        } else if (diffDay > 3 && diffDay <= 30) {
            if (bookBorrow.getBorrowSeq() <= 2) {
                overdueFine = (diffDay - 3) * 0.5f;
            } else {
                overdueFine = (diffDay - 3) * 1f;
            }
        } else if (diffDay > 30) {
            overdueFine = 40f;
        }

        return overdueFine;
    }

    @Override
    public FieldVo getBorrowStatus(int stauts, Timestamp expireTime) {
        //todo 未判断超时
        FieldVo model = new FieldVo();
        if (stauts == 1000) {
            return new FieldVo(1000, "借阅中");
        } else if (stauts == 2000) {
            return new FieldVo(2000, "已超期");
        } else if (stauts == 3000)
            return new FieldVo(3000, "已归还");
        else if (stauts == 4000)
            return new FieldVo(4000, "已购买");
        return model;
    }

    @Override
    public FieldVo getReturnWay(int way) {
        FieldVo model=new FieldVo();
        if(way==1)
            return new FieldVo(1,"自助设备");
        return model;
    }

    @Override
    public FieldVo getIdentityType(int type) {
        FieldVo model = new FieldVo();
        if (type == 1)
            return new FieldVo(1, "IC卡");
        else if (type == 2)
            return new FieldVo(2, "小程序");
        return model;
    }

    @Override
    public FieldVo getBorrowWay(int way) {
        FieldVo model=new FieldVo();
        if(way==1)
            return new FieldVo(1,"自助设备");
        return model;
    }

    @Override
    public FieldVo getFlowType(int type) {
        FieldVo model=new FieldVo();
        if(type==1)
            return new FieldVo(1,"借还");
        return model;
    }
}
