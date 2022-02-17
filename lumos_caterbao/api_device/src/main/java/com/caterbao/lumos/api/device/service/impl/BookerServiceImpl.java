package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.RetBookerBorrowReturn;
import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturn;
import com.caterbao.lumos.api.device.service.BookerService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public class BookerServiceImpl implements BookerService {

    @Override
    public CustomResult borrowReturn(String operater, RopBookerBorrowReturn rop) {

        RetBookerBorrowReturn ret = new RetBookerBorrowReturn();



        return CustomResult.success("借还成功", ret);
    }
}
