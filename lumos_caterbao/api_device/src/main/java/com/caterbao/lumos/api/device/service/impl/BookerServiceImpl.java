package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.rop.model.BookBean;
import com.caterbao.lumos.api.device.service.BookerService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowReturnFlowMapper;
import com.caterbao.lumos.locals.dal.mapper.MerchDeviceMapper;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowReturnFlow;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookerServiceImpl implements BookerService {

    @Autowired
    private BookBorrowReturnFlowMapper bookBorrowReturnFlowMapper;

    @Autowired
    private MerchDeviceMapper merchDeviceMapper;

    @Override
    public CustomResult borrowReturnCreateFlow(String operater, RopBookerBorrowReturnCreateFlow rop) {

        LumosSelective selective_MerchDevice=new LumosSelective();
        selective_MerchDevice.addWhere("DeviceId",rop.getDeviceId());
        selective_MerchDevice.addWhere("BindStatus","1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);

        if (d_MerchDevice==null)
            return CustomResult.fail("设备未绑定商户");

        if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
            return CustomResult.fail("设备未绑定店铺");

        if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
            return CustomResult.fail("设备未绑定门店");

        BookBorrowReturnFlow d_BookBorrowReturnFlow = new BookBorrowReturnFlow();

        d_BookBorrowReturnFlow.setId(IdWork.generateGUID());
        d_BookBorrowReturnFlow.setMerchId(d_MerchDevice.getMerchId());
        d_BookBorrowReturnFlow.setStoreId(d_MerchDevice.getStoreId());
        d_BookBorrowReturnFlow.setShopId(d_MerchDevice.getShopId());
        d_BookBorrowReturnFlow.setDeviceId(rop.getDeviceId());
        d_BookBorrowReturnFlow.setCabinetId(rop.getCabinetId());
        d_BookBorrowReturnFlow.setSlotId(rop.getSlotId());
        d_BookBorrowReturnFlow.setIdentityType(rop.getIdentityType());
        d_BookBorrowReturnFlow.setIdentityId(rop.getIdentityId());
        d_BookBorrowReturnFlow.setClientUserId("");
        d_BookBorrowReturnFlow.setStatus(1);
        d_BookBorrowReturnFlow.setCreateTime(CommonUtil.getDateTimeNow());
        d_BookBorrowReturnFlow.setCreator(IdWork.generateGUID());

        if (bookBorrowReturnFlowMapper.insert(d_BookBorrowReturnFlow) <= 0)
            return CustomResult.fail("流程创建失败");


        RetBookerBorrowReturnCreateFlow ret = new RetBookerBorrowReturnCreateFlow();
        ret.setFlowId(d_BookBorrowReturnFlow.getId());

        return CustomResult.success("流程创建成功", ret);
    }

    @Override
    public CustomResult borrowReturnOpenAction(String operater, RopBookerBorrowReturnOpenAction rop) {

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

    @Override
    public CustomResult borrowReturnCloseAction(String operater, RopBookerBorrowReturnCloseAction rop) {

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
