package com.caterbao.lumos.locals.biz.service.impl;

import com.caterbao.lumos.locals.biz.model.BookerCountBorrowBookResult;
import com.caterbao.lumos.locals.biz.service.BookerService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.vo.FieldVo;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowMapper;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service("BizBookerService")
public class BookerServiceImpl implements BookerService {

    private BookBorrowMapper bookBorrowMapper;
    @Autowired(required = false)
    public void setBookBorrowMapper(BookBorrowMapper bookBorrowMapper) {
        this.bookBorrowMapper = bookBorrowMapper;
    }
    @Override
    public BookerCountBorrowBookResult CountBorrowBookResult(String clientUserId) {

        BookerCountBorrowBookResult result = new BookerCountBorrowBookResult();

        LumosSelective selective_1 = new LumosSelective();
        selective_1.setFields("*");
        selective_1.addWhere("ClientUserId", clientUserId);
        selective_1.addWhere("Statuss",new String[]{"1000","2000"});

        List<BookBorrow> d_BookBorrows = bookBorrowMapper.find(selective_1);

        float sumOverdueFine=0;

        int sumWilldueQuantity=0;
        int sumOverdueQuantity=0;
        for (int i = 0; i < d_BookBorrows.size(); i++) {
            BookBorrow d_BookBorrow = d_BookBorrows.get(i);


            if (chekIsWilldueBook(d_BookBorrow.getExpireTime())) {
                sumWilldueQuantity += 1;
            } else if (chekIsOverdueBook(d_BookBorrow.getExpireTime())) {
                sumOverdueQuantity += 1;

                float overdueFine = CalculateOverdueFine(d_BookBorrow.getExpireTime(),d_BookBorrow.getBorrowSeq());
                sumOverdueFine += overdueFine;
            }

        }

        result.setBorrowedQuantity(d_BookBorrows.size());
        result.setWilldueQuantity(sumWilldueQuantity);
        result.setOverdueQuantity(sumOverdueQuantity);
        result.setOverdueFine(sumOverdueFine);
        return result;
    }

    @Override
    public float CalculateOverdueFine(Timestamp expireTime,int seq) {

        long l = CommonUtil.getDateTimeNow().getTime() - expireTime.getTime();
        long diffDay = l / (24 * 60 * 60 * 1000);

        float overdueFine = 0;

        if (diffDay <= 3) {
            overdueFine = 0;
        } else if (diffDay > 3 && diffDay <= 30) {
            if (seq <= 2) {
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

    @Override
    public boolean chekIsWilldueBook(Timestamp expireTime) {
        boolean isflag = false;

        long l_Willdue = expireTime.getTime() - CommonUtil.getDateTimeNow().getTime();
        long diffDay_Willdue = l_Willdue / (24 * 60 * 60 * 1000);

        if (diffDay_Willdue >= 0 && diffDay_Willdue <= 7) {
            isflag = true;
        }

        return isflag;
    }

    @Override
    public boolean chekIsOverdueBook(Timestamp expireTime) {
        boolean isflag = false;

        long l_Overdue = CommonUtil.getDateTimeNow().getTime() - expireTime.getTime();
        long diffDay_Overdue = l_Overdue / (24 * 60 * 60 * 1000);

        if (diffDay_Overdue > 0) {
            isflag = true;
        }

        return isflag;
    }

    @Override
    public  boolean checkCanRenew(int renewedCount,int maxRenewCount){
        return  false;
    }

    @Override
    public boolean checkCanReturn(Timestamp expireTime,int seq,float skuPrice){
        return  false;
    }

    @Override
    public  boolean checkNeedPay(Timestamp expireTime,int seq,float skuPrice){
        return  false;
    }

}
