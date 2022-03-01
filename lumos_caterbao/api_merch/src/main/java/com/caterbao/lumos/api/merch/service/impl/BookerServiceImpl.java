package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopBookerBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBookerDeviceFeedback;
import com.caterbao.lumos.api.merch.rop.RopBookerRenewList;
import com.caterbao.lumos.api.merch.service.BookerService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.PageResult;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowFlowDataMapper;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowFlowMapper;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlow;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlowData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BookerServiceImpl implements BookerService {

    private BookBorrowFlowMapper bookBorrowFlowMapper;;

    private BookBorrowFlowDataMapper bookBorrowFlowDataMapper;

    private com.caterbao.lumos.locals.biz.service.BookerService bizBookerService;

    @Autowired
    public void setBookBorrowFlowMapper(BookBorrowFlowMapper bookBorrowFlowMapper) {
        this.bookBorrowFlowMapper = bookBorrowFlowMapper;
    }

    @Autowired
    public void setBookBorrowFlowDataMapper(BookBorrowFlowDataMapper bookBorrowFlowDataMapper) {
        this.bookBorrowFlowDataMapper = bookBorrowFlowDataMapper;
    }

    @Autowired
    public void setBizBookerService(com.caterbao.lumos.locals.biz.service.BookerService bizBookerService) {
        this.bizBookerService = bizBookerService;
    }

    @Override
    public CustomResult<Object> borrowList(String operater, String merchId, RopBookerBorrowList rop) {

        CustomResult<Object> result = new CustomResult<>();
        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize,"BorrowTime DESC");


        LumosSelective selective=new LumosSelective();
        selective.setFields("*");

        selective.addWhere("MerchId",merchId);

        List<BookBorrowFlowData> d_BookBorrowFlowDatas = bookBorrowFlowDataMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (BookBorrowFlowData  d_BookBorrowFlowData :
                d_BookBorrowFlowDatas ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_BookBorrowFlowData.getId());
            item.put("flowId",d_BookBorrowFlowData.getFlowId());
            item.put("identityType",bizBookerService.getIdentityType(d_BookBorrowFlowData.getIdentityType()));
            item.put("clientUserId","");
            item.put("clientFullName","");
            item.put("skuCumCode",d_BookBorrowFlowData.getSkuCumCode());
            item.put("skuName",d_BookBorrowFlowData.getSkuName());
            item.put("borrowWay",bizBookerService.getBorrowWay(d_BookBorrowFlowData.getBorrowWay()));
            item.put("borrowTime", CommonUtil.toDateTimeStr(d_BookBorrowFlowData.getBorrowTime()));
            item.put("returnWay",bizBookerService.getReturnWay(d_BookBorrowFlowData.getReturnWay()));
            item.put("returnTime", "");
            item.put("buyTime", "");
            item.put("renewLastTime","");
            item.put("renewCount","");
            item.put("borrowStatus",bizBookerService.getBorrowStatus(d_BookBorrowFlowData.getBorrowStatus()));//借阅，超期
            items.add(item);
        }

        long total = page.getTotal();
        PageResult<Object> ret = new PageResult<>();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return result.success("",ret);
    }

    @Override
    public CustomResult<Object> renewList(String operater, String merchId, RopBookerRenewList rop) {
        return null;
    }

    @Override
    public CustomResult<Object> deviceFeedback(String operater, String merchId, RopBookerDeviceFeedback rop) {

        CustomResult<Object> result = new CustomResult<>();
        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize,"CreateTime DESC");


        LumosSelective selective=new LumosSelective();
        selective.setFields("*");

        selective.addWhere("MerchId",merchId);

        List<BookBorrowFlow> d_BookBorrowFlows = bookBorrowFlowMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (BookBorrowFlow  d_BookBorrowFlow :
                d_BookBorrowFlows ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_BookBorrowFlow.getId());
            item.put("identityType",bizBookerService.getIdentityType(d_BookBorrowFlow.getIdentityType()));
            item.put("clientUserId","");
            item.put("clientFullName","");
            item.put("cabinetId",d_BookBorrowFlow.getCabinetId());
            item.put("slotId",d_BookBorrowFlow.getSlotId());
            item.put("openActionTime",CommonUtil.toDateTimeStr(d_BookBorrowFlow.getOpenActionTime()));
            item.put("openActionCode",d_BookBorrowFlow.getOpenActionCode());
            item.put("openActionResult",d_BookBorrowFlow.getOpenActionResult());
            item.put("closeActionTime",CommonUtil.toDateTimeStr(d_BookBorrowFlow.getCloseActionTime()));
            item.put("closeActionCode",d_BookBorrowFlow.getCloseActionCode());
            item.put("closeActionResult",d_BookBorrowFlow.getCloseActionResult());
            items.add(item);
        }

        long total = page.getTotal();
        PageResult<Object> ret = new PageResult<>();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return result.success("",ret);
    }

}
