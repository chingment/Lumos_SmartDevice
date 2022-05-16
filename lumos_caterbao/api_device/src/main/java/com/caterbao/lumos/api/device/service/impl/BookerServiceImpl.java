package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.rop.vo.BookerBorrowReturnActionData;
import com.caterbao.lumos.api.device.rop.vo.BookVo;
import com.caterbao.lumos.api.device.rop.vo.RfTagVo;
import com.caterbao.lumos.api.device.service.BookerService;
import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.*;
import com.caterbao.lumos.locals.dal.pojo.*;
import com.caterbao.lumos.locals.dal.vw.BookerDeviceSkuStockVw;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BookerServiceImpl implements BookerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private BookFlowMapper bookFlowMapper;
    private BookBorrowMapper bookBorrowMapper;
    private BookerStockMapper bookerStockMapper;
    private BookFlowLogMapper bookFlowLogMapper;
    private MerchDeviceMapper merchDeviceMapper;
    private IcCardMapper icCardMapper;

    private com.caterbao.lumos.locals.biz.service.BookerService bizBookerService;
    private CacheFactory cacheFactory;

    @Autowired(required = false)
    public void setBookFlowMapper(BookFlowMapper bookFlowMapper) {
        this.bookFlowMapper = bookFlowMapper;
    }

    @Autowired(required = false)
    public void setBookBorrowMapper(BookBorrowMapper bookBorrowMapper) {
        this.bookBorrowMapper = bookBorrowMapper;
    }

    @Autowired(required = false)
    public void setBookFlowLogMapper(BookFlowLogMapper bookFlowLogMapper) {
        this.bookFlowLogMapper = bookFlowLogMapper;
    }

    @Autowired(required = false)
    public void setMerchDeviceMapper(MerchDeviceMapper merchDeviceMapper) {
        this.merchDeviceMapper = merchDeviceMapper;
    }

    @Autowired(required = false)
    public void setBookerStockMapper(BookerStockMapper bookerStockMapper) {
        this.bookerStockMapper = bookerStockMapper;
    }

    @Autowired(required = false)
    public void setIcCardMapper(IcCardMapper icCardMapper) {
        this.icCardMapper = icCardMapper;
    }

    @Autowired
    public void setBizBookerService(com.caterbao.lumos.locals.biz.service.BookerService bizBookerService) {
        this.bizBookerService = bizBookerService;
    }

    @Autowired
    public void setCacheFactory(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }


    @Override
    public CustomResult<RetBookerCreateFlow> createFlow(String operater, RopBookerCreateFlow rop) {
        logger.info("createFlow");

        CustomResult<RetBookerCreateFlow> result = new CustomResult<>();

        if (rop.getType() == 0)
            return result.fail("创建失败[W01]");

        if (CommonUtil.isEmpty(rop.getDeviceId()))
            return result.fail("创建失败[W02]");

        if (rop.getType() == 1) {
            if (CommonUtil.isEmpty(rop.getSlotId()))
                return result.fail("创建失败[W03]");

            if (CommonUtil.isEmpty(rop.getClientUserId()))
                return result.fail("创建失败[W04]");

            if (CommonUtil.isEmpty(rop.getIdentityType()))
                return result.fail("创建失败[W05]");

            if (CommonUtil.isEmpty(rop.getIdentityId()))
                return result.fail("创建失败[W06]");
        }

        LumosSelective selective_MerchDevice = new LumosSelective();
        selective_MerchDevice.setFields("*");
        selective_MerchDevice.addWhere("DeviceId", rop.getDeviceId());
        selective_MerchDevice.addWhere("BindStatus", "1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);

        if (d_MerchDevice == null)
            return result.fail("创建失败[D01]");

        if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
            return result.fail("创建失败[D02]");

        if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
            return result.fail("创建失败[D03]");


        if (rop.getType() == 1) {
            LumosSelective selective_BookFlowExs = new LumosSelective();
            selective_BookFlowExs.setFields("*");
            selective_BookFlowExs.addWhere("MerchId", d_MerchDevice.getMerchId());
            selective_BookFlowExs.addWhere("StoreId", d_MerchDevice.getStoreId());
            selective_BookFlowExs.addWhere("ShopId", d_MerchDevice.getShopId());
            selective_BookFlowExs.addWhere("DeviceId", d_MerchDevice.getId());
            selective_BookFlowExs.addWhere("SlotId", rop.getSlotId());
            selective_BookFlowExs.addWhere("Status", "3000");

            List<BookFlow> d_BookFlowExs = bookFlowMapper.find(selective_BookFlowExs);

            if (d_BookFlowExs.size() > 0)
                return result.fail("该柜门存在异常使用情况[D04]");
        }

        BookFlow d_BookFlow = new BookFlow();
        d_BookFlow.setId(IdWork.buildLongId());
        d_BookFlow.setType(rop.getType());
        d_BookFlow.setMerchId(d_MerchDevice.getMerchId());
        d_BookFlow.setMerchName(d_MerchDevice.getMerchName());
        d_BookFlow.setStoreId(d_MerchDevice.getStoreId());
        d_BookFlow.setStoreName(d_MerchDevice.getStoreName());
        d_BookFlow.setShopId(d_MerchDevice.getShopId());
        d_BookFlow.setShopName(d_MerchDevice.getShopName());
        d_BookFlow.setDeviceId(rop.getDeviceId());
        d_BookFlow.setDeviceCumCode(d_MerchDevice.getCumCode());
        d_BookFlow.setSlotId(rop.getSlotId());
        d_BookFlow.setClientUserId(rop.getClientUserId());
        d_BookFlow.setIdentityType(rop.getIdentityType());
        d_BookFlow.setIdentityId(rop.getIdentityId());
        d_BookFlow.setIdentityName(bizBookerService.getIdentityName(rop.getIdentityType(), rop.getIdentityId()));
        d_BookFlow.setStatus(1000);
        d_BookFlow.setCreateTime(CommonUtil.getDateTimeNow());
        d_BookFlow.setCreator(IdWork.buildGuId());

        if (bookFlowMapper.insert(d_BookFlow) <= 0)
            return result.fail("创建失败[D04]");

        RetBookerCreateFlow ret = new RetBookerCreateFlow();
        ret.setFlowId(d_BookFlow.getId());

        return result.success("创建成功", ret);

    }

    @Override
    @Transactional
    public CustomResult<RetBookerBorrowReturn> borrowReturn(String operater, RopBookerBorrowReturn rop) {

        CustomResult<RetBookerBorrowReturn> result = new CustomResult<>();

        new Thread(() -> {
            addBorrowReturnFlowLog(rop.getMsgId(), rop.getMsgMode(), rop.getDeviceId(), rop.getFlowId(), rop.getActionCode(), rop.getActionData(), "", rop.getActionRemark(), rop.getActionTime());
        }).start();

        RetBookerBorrowReturn ret = new RetBookerBorrowReturn();
        ret.setFlowId(rop.getFlowId());

        try {
            String actionCode = rop.getActionCode();
            String flowId = rop.getFlowId();

            LumosSelective selective_BookFlow = new LumosSelective();
            selective_BookFlow.setFields("*");
            selective_BookFlow.addWhere("FlowId", flowId);

            BookFlow d_BookFlow = bookFlowMapper.findOne(selective_BookFlow);

            if (d_BookFlow == null) {
                return result.fail("验证失败[D01]");
            }

            if (actionCode.equals("request_open_auth")) {

                BookerBorrowReturnActionData actionData = JsonUtil.toObject(rop.getActionData(), new TypeReference<BookerBorrowReturnActionData>() {
                });

                List<String> openRfIds = actionData.getOpenRfIds();

                d_BookFlow.setOpenActionTime(CommonUtil.getDateTimeNow());
                d_BookFlow.setOpenRfIds(JsonUtil.getJson(openRfIds));

            } else if (actionCode.equals("open_success")) {
                d_BookFlow.setStatus(3000);
            } else if (actionCode.equals("request_close_auth")) {

                BookerBorrowReturnActionData actionData = JsonUtil.toObject(rop.getActionData(), new TypeReference<BookerBorrowReturnActionData>() {
                });

                List<String> closeRfIds = actionData.getCloseRfIds();

                List<BookVo> ret_BorrowBooks = new ArrayList<>();
                List<BookVo> ret_ReturnBooks = new ArrayList<>();

                d_BookFlow.setCloseActionTime(CommonUtil.getDateTimeNow());
                d_BookFlow.setCloseRfIds(JsonUtil.getJson(closeRfIds));

                List<String> openRfIds = JsonUtil.toObject(d_BookFlow.getOpenRfIds(), new TypeReference<List<String>>() {
                });

                if (openRfIds == null) {
                    openRfIds = new ArrayList<>();
                }

                if (closeRfIds == null) {
                    closeRfIds = new ArrayList<>();
                }

                List<String> borrow_RfIds = new ArrayList<>();

                for (String openRfId : openRfIds) {
                    if (!closeRfIds.contains(openRfId)) {
                        borrow_RfIds.add(openRfId);
                    }
                }

                List<String> return_RfIds = new ArrayList<>();

                for (String closeRfId : closeRfIds) {
                    if (!openRfIds.contains(closeRfId)) {
                        return_RfIds.add(closeRfId);
                    }
                }

                LumosSelective selective_IcCard = new LumosSelective();
                selective_IcCard.setFields("*");
                selective_IcCard.addWhere("ClientUserId", d_BookFlow.getClientUserId());
                selective_IcCard.addWhere("IcCardId", d_BookFlow.getIdentityId());
                IcCard d_IcCard = icCardMapper.findOne(selective_IcCard);

                int maxBorrowExpireDay = 5;
                if (d_IcCard != null) {
                    maxBorrowExpireDay = d_IcCard.getMaxBorrowExpireDay();
                }


                //借书
                for (int i = 0; i < borrow_RfIds.size(); i++) {

                    String borrow_RfId = borrow_RfIds.get(i);

                    SkuInfo r_Sku = cacheFactory.getProduct().getSkuInfoByRfId(d_BookFlow.getMerchId(), borrow_RfId);

                    if(r_Sku!=null&&!CommonUtil.isEmpty(r_Sku.getId())) {

                        LumosSelective selective_BookBorrow = new LumosSelective();
                        selective_BookBorrow.setFields("*");
                        selective_BookBorrow.addWhere("MerchId", d_BookFlow.getMerchId());
                        // selective_BookBorrow.addWhere("FlowId", flowId);
                        selective_BookBorrow.addWhere("SkuRfId", borrow_RfId);
                        selective_BookBorrow.addWhere("CanBorrow", "1");

                        BookBorrow d_BookBorrow = bookBorrowMapper.findOne(selective_BookBorrow);
                        if (d_BookBorrow == null) {
                            d_BookBorrow = new BookBorrow();
                            d_BookBorrow.setId(String.valueOf(d_BookFlow.getId()) + String.valueOf(i));
                            d_BookBorrow.setMerchId(d_BookFlow.getMerchId());
                            d_BookBorrow.setMerchName(d_BookFlow.getMerchName());
                            d_BookBorrow.setStoreId(d_BookFlow.getStoreId());
                            d_BookBorrow.setStoreName(d_BookFlow.getStoreName());
                            d_BookBorrow.setShopId(d_BookFlow.getShopId());
                            d_BookBorrow.setShopName(d_BookFlow.getShopName());
                            d_BookBorrow.setDeviceId(d_BookFlow.getDeviceId());
                            d_BookBorrow.setDeviceCumCode(d_BookFlow.getDeviceCumCode());
                            d_BookBorrow.setSlotId(d_BookFlow.getSlotId());
                            d_BookBorrow.setFlowId(d_BookFlow.getId());
                            d_BookBorrow.setIdentityType(d_BookFlow.getIdentityType());
                            d_BookBorrow.setIdentityId(d_BookFlow.getIdentityId());
                            d_BookBorrow.setIdentityName(d_BookFlow.getIdentityName());
                            d_BookBorrow.setClientUserId(d_BookFlow.getClientUserId());
                            d_BookBorrow.setSkuRfId(borrow_RfId);
                            d_BookBorrow.setSkuId(r_Sku.getId());
                            d_BookBorrow.setSkuName(r_Sku.getName());
                            d_BookBorrow.setSkuCumCode(r_Sku.getCumCode());
                            d_BookBorrow.setSkuImgUrl(r_Sku.getImgUrl());
                            d_BookBorrow.setSkuPrice(r_Sku.getSalePrice());
                            d_BookBorrow.setBorrowSeq(i);
                            d_BookBorrow.setBorrowWay(1);
                            d_BookBorrow.setBorrowTime(CommonUtil.getDateTimeNow());
                            d_BookBorrow.setStatus(1000);
                            d_BookBorrow.setRenewCount(0);
                            d_BookBorrow.setExpireTime(CommonUtil.getDateTimeNowAndAddDay(maxBorrowExpireDay));
                            d_BookBorrow.setCreator(IdWork.buildGuId());
                            d_BookBorrow.setCreateTime(CommonUtil.getDateTimeNow());

                            if (bookBorrowMapper.insert(d_BookBorrow) <= 0) {
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return result.fail("验证失败[D03]");
                            } else {
                                ret_BorrowBooks.add(new BookVo(d_BookBorrow.getSkuId(), d_BookBorrow.getSkuRfId(), d_BookBorrow.getSkuName(), d_BookBorrow.getDeviceCumCode(), d_BookBorrow.getSkuImgUrl()));
                            }
                        }
                    }
                }

                //还书
                for (String return_RfId : return_RfIds) {

                    LumosSelective selective_BookBorrow = new LumosSelective();
                    selective_BookBorrow.setFields("*");
                    selective_BookBorrow.addWhere("MerchId", d_BookFlow.getMerchId());
                    selective_BookBorrow.addWhere("SkuRfId", return_RfId);
                    selective_BookBorrow.addWhere("CanReturn", "1");

                    BookBorrow d_BookBorrow = bookBorrowMapper.findOne(selective_BookBorrow);
                    if (d_BookBorrow != null) {
                        d_BookBorrow.setReturnFlowId(flowId);
                        d_BookBorrow.setReturnWay(d_BookBorrow.getBorrowWay());
                        d_BookBorrow.setReturnTime(CommonUtil.getDateTimeNow());
                        d_BookBorrow.setStatus(3000);
                        d_BookBorrow.setMender(IdWork.buildGuId());
                        d_BookBorrow.setMendTime(CommonUtil.getDateTimeNow());

                        if (bookBorrowMapper.update(d_BookBorrow) <= 0) {
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return result.fail("验证失败[D04]");
                        } else {
                            ret_ReturnBooks.add(new BookVo(d_BookBorrow.getSkuId(), d_BookBorrow.getSkuRfId(), d_BookBorrow.getSkuName(), d_BookBorrow.getDeviceCumCode(), d_BookBorrow.getSkuImgUrl()));
                        }
                    }
                }

                ret.setBorrowBooks(ret_BorrowBooks);
                ret.setReturnBooks(ret_ReturnBooks);

                d_BookFlow.setStatus(4000);
            }

            Timestamp lastActionTime= CommonUtil.toDateTimeTimestamp(rop.getActionTime());

            if(d_BookFlow.getLastActionTime()==null) {
                d_BookFlow.setLastActionCode(rop.getActionCode());
                d_BookFlow.setLastActionTime(CommonUtil.toDateTimeTimestamp(rop.getActionTime()));
                d_BookFlow.setLastActionRemark(rop.getActionRemark());
            }
            else {
                if (lastActionTime.after(d_BookFlow.getLastActionTime())) {
                    d_BookFlow.setLastActionCode(rop.getActionCode());
                    d_BookFlow.setLastActionTime(CommonUtil.toDateTimeTimestamp(rop.getActionTime()));
                    d_BookFlow.setLastActionRemark(rop.getActionRemark());
                }
            }

            d_BookFlow.setMendTime(CommonUtil.getDateTimeNow());
            d_BookFlow.setMender(IdWork.buildGuId());

            if (bookFlowMapper.update(d_BookFlow) <= 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result.fail("验证失败[D02]");
            }

            return result.success("验证成功", ret);
        } catch (Exception ex) {
            logger.error("借还流程发生异常", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result.fail("验证失败[99]");
        }
    }

    @Override
    public CustomResult<RetBookerSawBorrowBooks> sawBorrowBooks(String operater, RopBookerSawBorrowBooks rop) {

        CustomResult<RetBookerSawBorrowBooks> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        Page<?> page = PageHelper.startPage(pageNum, pageSize, "CreateTime Desc");

        LumosSelective selective_BookBorrows = new LumosSelective();
        selective_BookBorrows.setFields("*");
        selective_BookBorrows.addWhere("ClientUserId", rop.getClientUserId());
        selective_BookBorrows.addWhere("Status", "1000");
        List<BookBorrow> d_BookBorrows = bookBorrowMapper.find(selective_BookBorrows);

        List<Object> items = new ArrayList<>();

        for (BookBorrow d_BookBorrow :
                d_BookBorrows) {
            items.add(bizBookerService.covertBorrowBookVo(d_BookBorrow));
        }

        long total = page.getTotal();
        RetBookerSawBorrowBooks ret = new RetBookerSawBorrowBooks();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return result.success("", ret);

    }

    @Override
    @Transactional
    public CustomResult<RetBookerRenewBooks> renewBooks(String operater, RopBookerRenewBooks rop) {

        CustomResult<RetBookerRenewBooks> result = new CustomResult<>();
        try {
            String actionCode = rop.getActionCode();

            List<BookBorrow> d_BookBorrows = null;

            LumosSelective selective_BookBorrows = new LumosSelective();
            selective_BookBorrows.setFields("*");
            selective_BookBorrows.addWhere("ClientUserId", rop.getClientUserId());
            selective_BookBorrows.addWhere("Status", "1000");
            selective_BookBorrows.addWhere("CanRenew", "1");

            if (actionCode.equals("multi")) {
                selective_BookBorrows.addWhere("BorrowIds", rop.getBorrowIds());
            } else if (actionCode.equals("all")) {

            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result.fail("续借失败[01]");
            }

            d_BookBorrows = bookBorrowMapper.find(selective_BookBorrows);

            if (d_BookBorrows == null || d_BookBorrows.size() <= 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result.fail("续借失败[02]，没有可续借的书本");
            }

            LumosSelective selective_IcCard = new LumosSelective();
            selective_IcCard.setFields("*");
            selective_IcCard.addWhere("ClientUserId", rop.getClientUserId());
            selective_IcCard.addWhere("IcCardId", rop.getIdentityId());
            IcCard d_IcCard = icCardMapper.findOne(selective_IcCard);

            int maxBorrowRenewDay = 5;
            if (d_IcCard != null) {
                maxBorrowRenewDay = d_IcCard.getMaxBorrowRenewDay();
            }

            for (BookBorrow d_BookBorrow : d_BookBorrows) {
                if (bizBookerService.checkCanRenew(d_BookBorrow.getExpireTime(), d_BookBorrow.getRenewCount(), 1)) {
                    int renewCount = d_BookBorrow.getRenewCount() + 1;
                    d_BookBorrow.setExpireTime(CommonUtil.getDateTimeNowAndAddDay(maxBorrowRenewDay));
                    d_BookBorrow.setRenewLastTime(CommonUtil.getDateTimeNow());
                    d_BookBorrow.setRenewCount(renewCount);
                    d_BookBorrow.setMender(operater);
                    d_BookBorrow.setMendTime(CommonUtil.getDateTimeNow());

                    if (bookBorrowMapper.update(d_BookBorrow) < 0) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return result.fail("续借失败[03]");
                    }
                }
            }

            logger.info("续借成功");

            return result.success("续借成功");
        } catch (Exception ex) {
            logger.error("续借书本发生异常", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result.fail("续借失败[99]");
        }
    }

    @Override
    public CustomResult<RetBookerDisplayBooks> displayBooks(String operater, RopBookerDisplayBooks rop) {

        CustomResult<RetBookerDisplayBooks> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        LumosSelective selective_MerchDevice = new LumosSelective();
        selective_MerchDevice.addWhere("DeviceId", rop.getDeviceId());
        selective_MerchDevice.addWhere("BindStatus", "1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);

        Page<?> page = PageHelper.startPage(pageNum, pageSize, "SkuId Desc");

        LumosSelective selective_BookerStocks = new LumosSelective();
        selective_BookerStocks.setFields("*");
        selective_BookerStocks.addWhere("MerchId", d_MerchDevice.getMerchId());
        selective_BookerStocks.addWhere("StoreId", d_MerchDevice.getStoreId());
        selective_BookerStocks.addWhere("ShopId", d_MerchDevice.getShopId());
        selective_BookerStocks.addWhere("DeviceId", rop.getDeviceId());

        List<BookerDeviceSkuStockVw> d_BookerDeviceSkuStocksVw = bookerStockMapper.findDevcieSkuStock(selective_BookerStocks);

        List<Object> items = new ArrayList<>();

        for (BookerDeviceSkuStockVw d_BookerDeviceSkuStockVw :
                d_BookerDeviceSkuStocksVw) {

            HashMap<String, Object> item = new HashMap<>();

            SkuInfo r_SkuInfo = cacheFactory.getProduct().getSkuInfo(d_BookerDeviceSkuStockVw.getMerchId(), d_BookerDeviceSkuStockVw.getSkuId());
            if (r_SkuInfo != null) {
                item.put("skuId", r_SkuInfo.getId());
                item.put("name", r_SkuInfo.getName());
                item.put("imgUrl", r_SkuInfo.getImgUrl());
                item.put("cumCode", r_SkuInfo.getCumCode());
                item.put("quantity", d_BookerDeviceSkuStockVw.getQuantity());
                items.add(item);
            }
        }

        long total = page.getTotal();
        RetBookerDisplayBooks ret = new RetBookerDisplayBooks();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return result.success("", ret);

    }

    @Override
    @Transactional
    public CustomResult<RetBookerTakeStock> takeStock(String operater, RopBookerTakeStock rop) {

        CustomResult<RetBookerTakeStock> result = new CustomResult<>();

        new Thread(() -> {
            addBorrowReturnFlowLog(rop.getMsgId(), rop.getMsgMode(), rop.getDeviceId(), rop.getFlowId(), rop.getActionCode(), rop.getActionData(), "", rop.getActionRemark(), rop.getActionTime());
        }).start();

        try {
            RetBookerTakeStock ret = new RetBookerTakeStock();

            LumosSelective selective_MerchDevice = new LumosSelective();
            selective_MerchDevice.setFields("*");
            selective_MerchDevice.addWhere("DeviceId", rop.getDeviceId());
            selective_MerchDevice.addWhere("BindStatus", "1");
            MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);

            if (d_MerchDevice == null)
                return result.fail("盘点失败[D01]");

            if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
                return result.fail("盘点失败[D02]");

            if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
                return result.fail("盘点失败[D03]");

            bookerStockMapper.delete(d_MerchDevice.getMerchId(), d_MerchDevice.getStoreId(), d_MerchDevice.getShopId(), rop.getDeviceId());

            List<BookerStock> d_BookerStocks = new ArrayList<>();

            for (RfTagVo rfTag : rop.getRfTags()) {
                BookerStock d_BookerStock = new BookerStock();
                d_BookerStock.setId(IdWork.buildGuId());
                d_BookerStock.setMerchId(d_MerchDevice.getMerchId());
                d_BookerStock.setStoreId(d_MerchDevice.getStoreId());
                d_BookerStock.setShopId(d_MerchDevice.getShopId());
                d_BookerStock.setDeviceId(rop.getDeviceId());
                d_BookerStock.setSlotId(rfTag.getSlotId());
                d_BookerStock.setSkuRfId(rfTag.getRfId());
                d_BookerStock.setCreateTime(CommonUtil.getDateTimeNow());
                d_BookerStock.setCreator(operater);
                d_BookerStocks.add(d_BookerStock);
            }

            bookerStockMapper.insertBatch(d_BookerStocks);

            return result.success("盘点成功", ret);

        } catch (Exception ex) {
            logger.error("盘点流程发生异常", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result.fail("盘点失败[99]");
        }

    }

    private void addBorrowReturnFlowLog(String msgId, String msgMode, String deviceId, String flowId, String actionCode, String actionData, String actionResult, String actionRemark, String actionTime) {

        String merchId = null;
        String deviceCumCode = null;
        LumosSelective selective_MerchDevice = new LumosSelective();
        selective_MerchDevice.addWhere("DeviceId", deviceId);
        selective_MerchDevice.addWhere("BindStatus", "1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);
        if (d_MerchDevice != null) {
            merchId = d_MerchDevice.getMerchId();
            deviceCumCode = d_MerchDevice.getCumCode();
        }

        LumosSelective selective_BookFlowLog = new LumosSelective();
        selective_BookFlowLog.addWhere("MerchId", merchId);
        selective_BookFlowLog.addWhere("DeviceId", deviceId);
        selective_BookFlowLog.addWhere("FlowId", flowId);
        selective_BookFlowLog.addWhere("ActionCode", actionCode);
        BookFlowLog d_BookFlowLog= bookFlowLogMapper.findOne(selective_BookFlowLog);

        if(d_BookFlowLog==null) {
            d_BookFlowLog = new BookFlowLog();
            d_BookFlowLog.setId(IdWork.buildLongId());
            d_BookFlowLog.setMsgId(msgId);
            d_BookFlowLog.setMsgMode(msgMode);
            d_BookFlowLog.setMerchId(merchId);
            d_BookFlowLog.setDeviceId(deviceId);
            d_BookFlowLog.setDeviceCumCode(deviceCumCode);
            d_BookFlowLog.setFlowId(flowId);
            d_BookFlowLog.setActionCode(actionCode);
            d_BookFlowLog.setActionData(actionData);
            d_BookFlowLog.setActionResult(actionResult);
            d_BookFlowLog.setActionRemark(actionRemark);
            d_BookFlowLog.setActionTime(CommonUtil.toDateTimeTimestamp(actionTime));
            d_BookFlowLog.setCreator(IdWork.buildGuId());
            d_BookFlowLog.setCreateTime(CommonUtil.getDateTimeNow());
            bookFlowLogMapper.insert(d_BookFlowLog);
        }
        else
        {
            d_BookFlowLog.setActionData(actionData);
            d_BookFlowLog.setActionResult(actionResult);
            d_BookFlowLog.setActionRemark(actionRemark);
            d_BookFlowLog.setActionTime(CommonUtil.toDateTimeTimestamp(actionTime));

            bookFlowLogMapper.update(d_BookFlowLog);
        }
    }

}