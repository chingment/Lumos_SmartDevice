package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopBookerBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBookerFlowList;
import com.caterbao.lumos.api.merch.rop.RopBookerRenewList;
import com.caterbao.lumos.api.merch.service.BookerService;
import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.common.PageResult;
import com.caterbao.lumos.locals.dal.DeviceVoUtil;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowMapper;
import com.caterbao.lumos.locals.dal.mapper.BookFlowLogMapper;
import com.caterbao.lumos.locals.dal.mapper.BookFlowMapper;
import com.caterbao.lumos.locals.dal.mapper.SysClientUserMapper;
import com.caterbao.lumos.locals.dal.pojo.BookFlow;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;
import com.caterbao.lumos.locals.dal.pojo.BookFlowLog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BookerServiceImpl implements BookerService {

    private BookFlowMapper bookFlowMapper;;
    private BookFlowLogMapper bookFlowLogMapper;;
    private BookBorrowMapper bookBorrowMapper;
    private SysClientUserMapper sysClientUserMapper;
    private CacheFactory cacheFactory;
    private com.caterbao.lumos.locals.biz.service.BookerService bizBookerService;

    @Autowired
    public void setBookFlowMapper(BookFlowMapper bookFlowMapper) {
        this.bookFlowMapper = bookFlowMapper;
    }

    @Autowired
    public void setBookBorrowMapper(BookBorrowMapper bookBorrowMapper) {
        this.bookBorrowMapper = bookBorrowMapper;
    }

    @Autowired
    public void setBookFlowogMapper(BookFlowLogMapper bookFlowLogMapper) {
        this.bookFlowLogMapper = bookFlowLogMapper;
    }

    @Autowired
    public void setCacheFactory(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
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
        selective.addWhere("LikeFlowId",rop.getFlowId());
        selective.addWhere("LikeDeviceCode",rop.getDeviceCode());
        selective.addWhere("LikeSkuName",rop.getSkuName());

        if(rop.getBorrowTimeArea()!=null) {
            if(rop.getBorrowTimeArea().size()>=1){
                selective.addWhere("MinBorrowTime",rop.getBorrowTimeArea().get(0));
            }
            if(rop.getBorrowTimeArea().size()>=2) {
                selective.addWhere("MaxBorrowTime", rop.getBorrowTimeArea().get(1));
            }
        }

        if(rop.getReturnTimeArea()!=null) {
            if(rop.getReturnTimeArea().size()>=1){
                selective.addWhere("MinReturnTime",rop.getReturnTimeArea().get(0));
            }
            if(rop.getReturnTimeArea().size()>=2) {
                selective.addWhere("MaxReturnTime", rop.getReturnTimeArea().get(1));
            }
        }


        List<BookBorrow> d_BookBorrows = bookBorrowMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (BookBorrow d_BookBorrow :
                d_BookBorrows) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id", d_BookBorrow.getId());
            item.put("flowId", d_BookBorrow.getFlowId());
            item.put("identityType",bizBookerService.getIdentityType(d_BookBorrow.getIdentityType()));
            item.put("identityId", d_BookBorrow.getIdentityId());
            item.put("identityName", d_BookBorrow.getIdentityName());
            item.put("deviceCode", DeviceVoUtil.getCode(d_BookBorrow.getDeviceId(), d_BookBorrow.getDeviceCumCode()));
            item.put("skuCumCode", d_BookBorrow.getSkuCumCode());
            item.put("skuName", d_BookBorrow.getSkuName());
            item.put("borrowWay",bizBookerService.getBorrowWay(d_BookBorrow.getBorrowWay()));
            item.put("borrowTime", CommonUtil.toDateStr(d_BookBorrow.getBorrowTime()));
            item.put("expireTime", CommonUtil.toDateStr(d_BookBorrow.getExpireTime()));
            item.put("returnWay",bizBookerService.getReturnWay(d_BookBorrow.getReturnWay()));
            item.put("returnTime", CommonUtil.toDateStr(d_BookBorrow.getReturnTime()));
            item.put("renewLastTime","");
            item.put("renewCount","");
            item.put("status",bizBookerService.getBorrowStatus(d_BookBorrow.getStatus(), d_BookBorrow.getExpireTime()));//借阅，超期
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
    public CustomResult<Object> borrowDetails(String operater, String merchId, String borrowId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId", merchId);
        selective.addWhere("BorrowId", borrowId);
        BookBorrow d_BookBorrow = bookBorrowMapper.findOne(selective);

        if (d_BookBorrow == null)
            return result.fail("找不到数据");

        HashMap<String, Object> ret = new HashMap<>();
        ret.put("id", d_BookBorrow.getId());
        ret.put("flowId", d_BookBorrow.getFlowId());
        ret.put("identityType", bizBookerService.getIdentityType(d_BookBorrow.getIdentityType()));
        ret.put("identityId", d_BookBorrow.getIdentityId());
        ret.put("identityName", d_BookBorrow.getIdentityName());
        ret.put("deviceCode", DeviceVoUtil.getCode(d_BookBorrow.getDeviceId(), d_BookBorrow.getDeviceCumCode()));
        ret.put("skuRfId", d_BookBorrow.getSkuRfId());
        ret.put("skuImgUrl", d_BookBorrow.getSkuImgUrl());
        ret.put("skuCumCode", d_BookBorrow.getSkuCumCode());
        ret.put("skuName", d_BookBorrow.getSkuName());
        ret.put("borrowWay", bizBookerService.getBorrowWay(d_BookBorrow.getBorrowWay()));
        ret.put("borrowTime", CommonUtil.toDateStr(d_BookBorrow.getBorrowTime()));
        ret.put("expireTime", CommonUtil.toDateStr(d_BookBorrow.getExpireTime()));
        ret.put("renewLastTime", CommonUtil.toDateStr(d_BookBorrow.getRenewLastTime()));
        ret.put("renewCount", d_BookBorrow.getRenewCount());
        ret.put("status", bizBookerService.getBorrowStatus(d_BookBorrow.getStatus(), d_BookBorrow.getExpireTime()));//借阅，超期

        HashMap<String, Object> returnDetails = new HashMap<>();
        String returnDeviceCode = "";
        String returnIdentityName = "";
        if (!CommonUtil.isEmpty(d_BookBorrow.getReturnFlowId())) {
            selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("MerchId", merchId);
            selective.addWhere("FlowId", d_BookBorrow.getReturnFlowId());
            BookFlow d_BookFlowByReturn = bookFlowMapper.findOne(selective);
            if (d_BookFlowByReturn != null) {
                returnDeviceCode = DeviceVoUtil.getCode(d_BookFlowByReturn.getDeviceId(), d_BookFlowByReturn.getDeviceCumCode());
                returnIdentityName = d_BookFlowByReturn.getIdentityName();
            }
        }

        ret.put("returnFlowId", d_BookBorrow.getReturnFlowId());
        ret.put("returnWay", bizBookerService.getBorrowWay(d_BookBorrow.getReturnWay()));
        ret.put("returnTime", CommonUtil.toDateTimeStr(d_BookBorrow.getReturnTime()));
        ret.put("returnDeviceCode", returnDeviceCode);
        ret.put("returnIdentityName", returnIdentityName);

        return result.success("", ret);
    }

    @Override
    public CustomResult<Object> renewList(String operater, String merchId, RopBookerRenewList rop) {
        return null;
    }

    @Override
    public CustomResult<Object> flowList(String operater, String merchId, RopBookerFlowList rop) {

        CustomResult<Object> result = new CustomResult<>();
        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize,"CreateTime DESC");


        LumosSelective selective=new LumosSelective();
        selective.setFields("*");

        selective.addWhere("MerchId",merchId);
        selective.addWhere("ActionCode",rop.getActionCode());

        selective.addWhere("LikeFlowId",rop.getFlowId());
        selective.addWhere("LikeDeviceCode",rop.getDeviceCode());


        if(rop.getCreateTimeArea()!=null) {
            if(rop.getCreateTimeArea().size()>=1){
                selective.addWhere("MinCreateTime",rop.getCreateTimeArea().get(0));
            }
            if(rop.getCreateTimeArea().size()>=2) {
                selective.addWhere("MaxCreateTime", rop.getCreateTimeArea().get(1));
            }
        }

        List<BookFlow> d_BookFlows = bookFlowMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (BookFlow d_BookFlow :
                d_BookFlows) {

            HashMap<String, Object> item = new HashMap<>();

            item.put("id", d_BookFlow.getId());
            item.put("deviceCode", DeviceVoUtil.getCode(d_BookFlow.getDeviceId(), d_BookFlow.getDeviceCumCode()));
            item.put("flowType",bizBookerService.getFlowType(d_BookFlow.getFlowType()));
            item.put("lastActionCode",d_BookFlow.getLastActionCode());
            item.put("lastActionTime", CommonUtil.toDateTimeStr(d_BookFlow.getLastActionTime()));
            item.put("lastActionRemark",d_BookFlow.getLastActionRemark());
            item.put("createTime",CommonUtil.toDateTimeStr(d_BookFlow.getCreateTime()));
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
    public CustomResult<Object> flowDetails(String operater, String merchId, String flowId) {

        CustomResult<Object> result = new CustomResult<>();

        HashMap<String, Object> ret = new HashMap<>();

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId", merchId);
        selective.addWhere("FlowId", flowId);

        BookFlow d_BookFlow = bookFlowMapper.findOne(selective);

        if(d_BookFlow==null)
            result.fail("找不到记录");

        List<String>  open_RfIds= JsonUtil.toObject(d_BookFlow.getOpenRfIds(), new TypeReference<List<String>>() {
        });

        List<Object> openSkus=new ArrayList<>();

        if(open_RfIds!=null&&open_RfIds.size()>0) {

            for (int i = 0; i < open_RfIds.size(); i++) {
                String open_RfId = open_RfIds.get(i);

                SkuInfo r_Sku = cacheFactory.getProduct().getSkuInfoByRfId(d_BookFlow.getMerchId(), open_RfId);

                HashMap<String,String> openSku=new HashMap<>();
                openSku.put("id",r_Sku.getId());
                openSku.put("cumCode",r_Sku.getCumCode());
                openSku.put("name",r_Sku.getName());
                openSku.put("rfId",open_RfId);
                openSku.put("imgUrl",r_Sku.getImgUrl());
                openSkus.add(openSku);
            }

        }

        List<String>  close_RfIds= JsonUtil.toObject(d_BookFlow.getCloseRfIds(), new TypeReference<List<String>>() {
        });

        List<Object> closeSkus=new ArrayList<>();

        if(close_RfIds!=null&&close_RfIds.size()>0) {

            for (int i = 0; i < close_RfIds.size(); i++) {

                String close_RfId = close_RfIds.get(i);

                SkuInfo r_Sku = cacheFactory.getProduct().getSkuInfoByRfId(d_BookFlow.getMerchId(), close_RfId);

                HashMap<String,String> closeSku=new HashMap<>();

                closeSku.put("id",r_Sku.getId());
                closeSku.put("cumCode",r_Sku.getCumCode());
                closeSku.put("name",r_Sku.getName());
                closeSku.put("rfId",close_RfId);
                closeSku.put("imgUrl",r_Sku.getImgUrl());

                closeSkus.add(closeSku);
            }
        }

        selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId", merchId);
        selective.addWhere("FlowId", flowId);

        List<BookFlowLog> d_BookFlowLogs = bookFlowLogMapper.find(selective);

        List<Object> flowLogs=new ArrayList<>();

        for (BookFlowLog d_BookFlowLog :
                d_BookFlowLogs) {

            HashMap<String, Object> flowLog = new HashMap<>();

            flowLog.put("actionCode", d_BookFlowLog.getActionCode());
            flowLog.put("actionTime",CommonUtil.toDateTimeStr(d_BookFlowLog.getActionTime()));
            flowLog.put("actionRemark", d_BookFlowLog.getActionRemark());

            flowLogs.add(flowLog);
        }

        ret.put("openSkus",openSkus);
        ret.put("closeSkus",closeSkus);
        ret.put("flowType",d_BookFlow.getFlowType());
        ret.put("flowLogs",flowLogs);


        return result.success("", ret);
    }

}
