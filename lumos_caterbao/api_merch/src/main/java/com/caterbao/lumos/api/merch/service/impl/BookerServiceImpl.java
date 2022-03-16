package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopBookerBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBookerDeviceFeedback;
import com.caterbao.lumos.api.merch.rop.RopBookerRenewList;
import com.caterbao.lumos.api.merch.service.BookerService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.PageResult;
import com.caterbao.lumos.locals.dal.DeviceVoUtil;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowFlowDataMapper;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowFlowLogMapper;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowFlowMapper;
import com.caterbao.lumos.locals.dal.mapper.SysClientUserMapper;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlow;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlowData;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlowLog;
import com.caterbao.lumos.locals.dal.pojo.SysClientUser;
import com.caterbao.lumos.locals.dal.vw.ClientUserVw;
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
    private BookBorrowFlowLogMapper bookBorrowFlowLogMapper;;
    private BookBorrowFlowDataMapper bookBorrowFlowDataMapper;
    private SysClientUserMapper sysClientUserMapper;

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
    public void setBookBorrowFlowLogMapper(BookBorrowFlowLogMapper bookBorrowFlowLogMapper) {
        this.bookBorrowFlowLogMapper = bookBorrowFlowLogMapper;
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
            item.put("identityId",d_BookBorrowFlowData.getIdentityId());
            item.put("identityName",d_BookBorrowFlowData.getIdentityName());
            item.put("deviceCode", DeviceVoUtil.getCode(d_BookBorrowFlowData.getDeviceId(),d_BookBorrowFlowData.getDeviceCumCode()));
            item.put("skuCumCode",d_BookBorrowFlowData.getSkuCumCode());
            item.put("skuName",d_BookBorrowFlowData.getSkuName());
            item.put("borrowWay",bizBookerService.getBorrowWay(d_BookBorrowFlowData.getBorrowWay()));
            item.put("borrowTime", CommonUtil.toDateTimeStr(d_BookBorrowFlowData.getBorrowTime()));
            item.put("expireTime", CommonUtil.toDateTimeStr(d_BookBorrowFlowData.getExpireTime()));
            item.put("returnWay",bizBookerService.getReturnWay(d_BookBorrowFlowData.getReturnWay()));
            item.put("returnTime", "");
            item.put("buyTime", "");
            item.put("renewLastTime","");
            item.put("renewCount","");
            item.put("borrowStatus",bizBookerService.getBorrowStatus(d_BookBorrowFlowData.getBorrowStatus(),d_BookBorrowFlowData.getExpireTime()));//借阅，超期
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
    public CustomResult<Object> borrowDetails(String operater, String merchId, String flowDataId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective_FlowData = new LumosSelective();
        selective_FlowData.setFields("*");

        selective_FlowData.addWhere("MerchId", merchId);
        selective_FlowData.addWhere("FlowDataId", flowDataId);

        BookBorrowFlowData d_BookBorrowFlowData = bookBorrowFlowDataMapper.findOne(selective_FlowData);

        if (d_BookBorrowFlowData == null)
            return result.fail("找不到数据");

        HashMap<String, Object> ret = new HashMap<>();
        ret.put("id",d_BookBorrowFlowData.getId());
        ret.put("flowId",d_BookBorrowFlowData.getFlowId());
        ret.put("identityType",bizBookerService.getIdentityType(d_BookBorrowFlowData.getIdentityType()));
        ret.put("identityId",d_BookBorrowFlowData.getIdentityId());
        ret.put("identityName",d_BookBorrowFlowData.getIdentityName());
        ret.put("deviceCode", DeviceVoUtil.getCode(d_BookBorrowFlowData.getDeviceId(),d_BookBorrowFlowData.getDeviceCumCode()));
        ret.put("skuRfId",d_BookBorrowFlowData.getSkuRfId());
        ret.put("skuImgUrl",d_BookBorrowFlowData.getSkuImgUrl());
        ret.put("skuCumCode",d_BookBorrowFlowData.getSkuCumCode());
        ret.put("skuName",d_BookBorrowFlowData.getSkuName());
        ret.put("borrowWay",bizBookerService.getBorrowWay(d_BookBorrowFlowData.getBorrowWay()));
        ret.put("borrowTime", CommonUtil.toDateTimeStr(d_BookBorrowFlowData.getBorrowTime()));
        ret.put("expireTime", CommonUtil.toDateTimeStr(d_BookBorrowFlowData.getExpireTime()));
        ret.put("returnWay",bizBookerService.getReturnWay(d_BookBorrowFlowData.getReturnWay()));
        ret.put("returnTime", CommonUtil.toDateTimeStr(d_BookBorrowFlowData.getReturnTime()));
        ret.put("renewLastTime",CommonUtil.toDateTimeStr(d_BookBorrowFlowData.getRenewLastTime()));
        ret.put("renewCount",d_BookBorrowFlowData.getRenewCount());
        ret.put("borrowStatus",bizBookerService.getBorrowStatus(d_BookBorrowFlowData.getBorrowStatus(),d_BookBorrowFlowData.getExpireTime()));//借阅，超期


        return result.success("", ret);
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

            HashMap<String, Object> item = new HashMap<>();

            item.put("id", d_BookBorrowFlow.getId());
            item.put("deviceCode", DeviceVoUtil.getCode(d_BookBorrowFlow.getDeviceId(), d_BookBorrowFlow.getDeviceCumCode()));

            BookBorrowFlowLog d_BookBorrowFlowLog = bookBorrowFlowLogMapper.findLastFlowLog(d_BookBorrowFlow.getId());

            String actionCode="";
            String actionTime="";
            String actionRemark="";
            if(d_BookBorrowFlowLog!=null) {
                actionCode = d_BookBorrowFlowLog.getActionCode();
                actionTime = CommonUtil.toDateTimeStr(d_BookBorrowFlowLog.getActionTime());
                actionRemark = d_BookBorrowFlowLog.getActionRemark();
            }

            item.put("actionCode",actionCode);
            item.put("actionTime", actionTime);
            item.put("actionRemark",actionRemark);
            item.put("createTime",CommonUtil.toDateTimeStr(d_BookBorrowFlow.getCreateTime()));
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
    public CustomResult<Object> deviceFeedbackDetails(String operater, String merchId, String flowId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective_FlowLogs = new LumosSelective();
        selective_FlowLogs.setFields("*");
        selective_FlowLogs.addWhere("MerchId", merchId);
        selective_FlowLogs.addWhere("FlowId", flowId);

        List<BookBorrowFlowLog> d_BookBorrowFlowLogs = bookBorrowFlowLogMapper.find(selective_FlowLogs);

        List<Object> flowLogs=new ArrayList<>();

        for (BookBorrowFlowLog  d_BookBorrowFlowLog :
                d_BookBorrowFlowLogs ) {

            HashMap<String, Object> flowLog = new HashMap<>();

            flowLog.put("actionCode",d_BookBorrowFlowLog.getActionCode());
            flowLog.put("actionTime",CommonUtil.toDateTimeStr(d_BookBorrowFlowLog.getActionTime()));
            flowLog.put("actionRemark",d_BookBorrowFlowLog.getActionRemark());

            flowLogs.add(flowLog);
        }

        HashMap<String, Object> ret = new HashMap<>();
        ret.put("flowLogs",flowLogs);


        return result.success("", ret);
    }

}
