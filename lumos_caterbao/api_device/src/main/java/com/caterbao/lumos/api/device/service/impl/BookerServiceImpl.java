package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.RetBookerBorrowReturn;
import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturn;
import com.caterbao.lumos.api.device.rop.model.BookBean;
import com.caterbao.lumos.api.device.service.BookerService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookerServiceImpl implements BookerService {

    @Override
    public CustomResult borrowReturn(String operater, RopBookerBorrowReturn rop) {

        RetBookerBorrowReturn ret = new RetBookerBorrowReturn();


        List<BookBean> borrowBooks=new ArrayList<>();

        borrowBooks.add(new BookBean("1","1","安徒生童话故事","1","1"));
        borrowBooks.add(new BookBean("1","1","这个杀手不太冷静","1","1"));


        List<BookBean> returnBooks=new ArrayList<>();
        returnBooks.add(new BookBean("1","1","西游记","1","1"));
        returnBooks.add(new BookBean("1","1","红楼梦","1","1"));

        ret.setBorrowBooks(borrowBooks);
        ret.setReturnBooks(returnBooks);

        return CustomResult.success("借还成功", ret);
    }
}
