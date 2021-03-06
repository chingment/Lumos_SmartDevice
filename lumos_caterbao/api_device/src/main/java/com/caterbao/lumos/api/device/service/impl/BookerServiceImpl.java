package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.rop.vo.*;
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

    private BookerSlotMapper bookerSlotMapper;
    private BookerTakeStockSheetMapper bookerTakeStockSheetMapper;
    private BookerTakeStockSheetItemMapper bookerTakeStockSheetItemMapper;
    private BookFlowMapper bookFlowMapper;
    private BookBorrowMapper bookBorrowMapper;
    private BookerStockMapper bookerStockMapper;
    private BookFlowLogMapper bookFlowLogMapper;
    private MerchDeviceMapper merchDeviceMapper;
    private IcCardMapper icCardMapper;

    private com.caterbao.lumos.locals.biz.service.BookerService bizBookerService;
    private CacheFactory cacheFactory;

    @Autowired(required = false)
    public void setBookerSlotMapper(BookerSlotMapper bookerSlotMapper) {
        this.bookerSlotMapper = bookerSlotMapper;
    }

    @Autowired(required = false)
    public void setBookerTakeStockSheetMapper(BookerTakeStockSheetMapper bookerTakeStockSheetMapper) {
        this.bookerTakeStockSheetMapper = bookerTakeStockSheetMapper;
    }

    @Autowired(required = false)
    public void setBookerTakeStockSheetItemMapper(BookerTakeStockSheetItemMapper bookerTakeStockSheetItemMapper) {
        this.bookerTakeStockSheetItemMapper = bookerTakeStockSheetItemMapper;
    }

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

    private MerchDeviceVw  getDevice(String deviceId){
        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("DeviceId", deviceId);
        selective.addWhere("BindStatus", "1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective);
        return d_MerchDevice;
    }

    @Override
    public CustomResult<RetBookerCreateFlow> createFlow(String operater, RopBookerCreateFlow rop) {
        logger.info("createFlow");

        CustomResult<RetBookerCreateFlow> result = new CustomResult<>();

        if (rop.getFlowType() == 0)
            return result.fail("????????????[W01]");

        if (CommonUtil.isEmpty(rop.getDeviceId()))
            return result.fail("????????????[W02]");

        if (rop.getFlowType() == 1) {
            if (CommonUtil.isEmpty(rop.getSlotId()))
                return result.fail("????????????[W03]");

            if (CommonUtil.isEmpty(rop.getFlowUserId()))
                return result.fail("????????????[W04]");

            if (CommonUtil.isEmpty(rop.getIdentityType()))
                return result.fail("????????????[W05]");

            if (CommonUtil.isEmpty(rop.getIdentityId()))
                return result.fail("????????????[W06]");
        }

        MerchDeviceVw d_MerchDevice = getDevice(rop.getDeviceId());

        if (d_MerchDevice == null)
            return result.fail("????????????[D01]");

        if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
            return result.fail("????????????[D02]");

        if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
            return result.fail("????????????[D03]");


        if (rop.getFlowType() == 1) {
            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("MerchId", d_MerchDevice.getMerchId());
            selective.addWhere("StoreId", d_MerchDevice.getStoreId());
            selective.addWhere("ShopId", d_MerchDevice.getShopId());
            selective.addWhere("DeviceId", d_MerchDevice.getId());
            selective.addWhere("SlotId", rop.getSlotId());
            selective.addWhere("Status", "3000");

            List<BookFlow> d_BookFlowExs = bookFlowMapper.find(selective);

            if (d_BookFlowExs.size() > 0)
                return result.fail("?????????????????????????????????[D04]");
        }

        BookFlow d_BookFlow = new BookFlow();
        d_BookFlow.setId(IdWork.buildLongId());
        d_BookFlow.setFlowType(rop.getFlowType());
        d_BookFlow.setMerchId(d_MerchDevice.getMerchId());
        d_BookFlow.setMerchName(d_MerchDevice.getMerchName());
        d_BookFlow.setStoreId(d_MerchDevice.getStoreId());
        d_BookFlow.setStoreName(d_MerchDevice.getStoreName());
        d_BookFlow.setShopId(d_MerchDevice.getShopId());
        d_BookFlow.setShopName(d_MerchDevice.getShopName());
        d_BookFlow.setDeviceId(rop.getDeviceId());
        d_BookFlow.setDeviceCumCode(d_MerchDevice.getCumCode());
        d_BookFlow.setSlotId(rop.getSlotId());
        d_BookFlow.setFlowUserId(rop.getFlowUserId());
        d_BookFlow.setIdentityType(rop.getIdentityType());
        d_BookFlow.setIdentityId(rop.getIdentityId());
        d_BookFlow.setIdentityName(bizBookerService.getIdentityName(rop.getIdentityType(), rop.getIdentityId()));
        d_BookFlow.setLastActionSn(0);
        d_BookFlow.setStatus(1000);
        d_BookFlow.setCreateTime(CommonUtil.getDateTimeNow());
        d_BookFlow.setCreator(IdWork.buildGuId());

        if (bookFlowMapper.insert(d_BookFlow) <= 0)
            return result.fail("????????????[D04]");

        RetBookerCreateFlow ret = new RetBookerCreateFlow();
        ret.setFlowId(d_BookFlow.getId());

        return result.success("????????????", ret);

    }

    @Override
    @Transactional
    public CustomResult<RetBookerBorrowReturn> borrowReturn(String operater, RopBookerBorrowReturn rop) {

        CustomResult<RetBookerBorrowReturn> result = new CustomResult<>();

        if(CommonUtil.isEmpty(rop.getFlowId())){
            return result.fail(2001, "????????????[D00]");
        }

        new Thread(() -> {
            addFlowLog(rop.getMsgId(), rop.getMsgMode(), rop.getDeviceId(), rop.getFlowId(),rop.getActionSn(), rop.getActionCode(), rop.getActionData(), "", rop.getActionRemark(), rop.getActionTime());
        }).start();

        RetBookerBorrowReturn ret = new RetBookerBorrowReturn();
        ret.setFlowId(rop.getFlowId());
        ret.setActionCode(rop.getActionCode());
        try {
            String actionCode = rop.getActionCode();
            String flowId = rop.getFlowId();

            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("FlowId", flowId);

            BookFlow d_BookFlow = bookFlowMapper.findOne(selective);

            if (d_BookFlow == null) {
                return result.fail(2001, "????????????[D01]");
            }

            if (actionCode.equals("request_open_auth")) {

                BookerBorrowReturnActionData actionData = JsonUtil.toObject(rop.getActionData(), new TypeReference<BookerBorrowReturnActionData>() {
                });

                List<String> openRfIds = actionData.getOpenRfIds();

                d_BookFlow.setOpenActionTime(CommonUtil.getDateTimeNow());
                d_BookFlow.setOpenRfIds(JsonUtil.getJson(openRfIds));
                d_BookFlow.setOpenRfIdsSize(openRfIds.size());

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
                d_BookFlow.setCloseRfIdsSize(closeRfIds.size());
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

                selective= new LumosSelective();
                selective.setFields("*");
                selective.addWhere("ClientUserId", d_BookFlow.getFlowUserId());
                selective.addWhere("IcCardId", d_BookFlow.getIdentityId());
                IcCard d_IcCard = icCardMapper.findOne(selective);

                int maxBorrowExpireDay = 5;
                if (d_IcCard != null) {
                    maxBorrowExpireDay = d_IcCard.getMaxBorrowExpireDay();
                }


                //??????
                for (int i = 0; i < borrow_RfIds.size(); i++) {
                    String borrow_RfId = borrow_RfIds.get(i);

                    long stockQuantity=bookerStockMapper.getSlotStockQuantityBySkuRfId(d_BookFlow.getMerchId(),d_BookFlow.getStoreId(),d_BookFlow.getShopId(),d_BookFlow.getDeviceId(), d_BookFlow.getSlotId(),borrow_RfId);

                    if(stockQuantity>0) {

                        SkuInfo r_Sku = cacheFactory.getProduct().getSkuInfoByRfId(d_BookFlow.getMerchId(), borrow_RfId);

                        if (r_Sku != null && !CommonUtil.isEmpty(r_Sku.getId())) {

                            selective = new LumosSelective();
                            selective.setFields("*");
                            selective.addWhere("MerchId", d_BookFlow.getMerchId());
                            selective.addWhere("SkuRfId", borrow_RfId);
                            selective.addWhere("CanBorrow", "1");

                            BookBorrow d_BookBorrow = bookBorrowMapper.findOne(selective);
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
                                d_BookBorrow.setClientUserId(d_BookFlow.getFlowUserId());
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

                                bookBorrowMapper.insert(d_BookBorrow);


                                bookerStockMapper.deleteSlotStockBySkuRfId(d_BookFlow.getMerchId(), d_BookFlow.getStoreId(), d_BookFlow.getShopId(), d_BookFlow.getDeviceId(),d_BookFlow.getSlotId(), d_BookBorrow.getSkuRfId());

                                ret_BorrowBooks.add(new BookVo(d_BookBorrow.getSkuId(), d_BookBorrow.getSkuRfId(), d_BookBorrow.getSkuName(), d_BookBorrow.getSkuCumCode(), d_BookBorrow.getSkuImgUrl()));

                            }
                        }
                    }
                }

                //??????
                for (String return_RfId : return_RfIds) {

                    selective = new LumosSelective();
                    selective.setFields("*");
                    selective.addWhere("MerchId", d_BookFlow.getMerchId());
                    selective.addWhere("SkuRfId", return_RfId);
                    selective.addWhere("CanReturn", "1");

                    BookBorrow d_BookBorrow = bookBorrowMapper.findOne(selective);
                    if (d_BookBorrow != null) {
                        d_BookBorrow.setReturnFlowId(flowId);
                        d_BookBorrow.setReturnWay(d_BookBorrow.getBorrowWay());
                        d_BookBorrow.setReturnTime(CommonUtil.getDateTimeNow());
                        d_BookBorrow.setStatus(3000);
                        d_BookBorrow.setMender(operater);
                        d_BookBorrow.setMendTime(CommonUtil.getDateTimeNow());

                        bookBorrowMapper.update(d_BookBorrow);

                        long stockQuantity = bookerStockMapper.getDeviceStockQuantityBySkuRfId(d_BookFlow.getMerchId(), d_BookFlow.getStoreId(), d_BookFlow.getShopId(), rop.getDeviceId(),d_BookBorrow.getSkuRfId());

                        if(stockQuantity==0) {
                            BookerStock d_BookerStock = new BookerStock();
                            d_BookerStock.setId(IdWork.buildGuId());
                            d_BookerStock.setMerchId(d_BookFlow.getMerchId());
                            d_BookerStock.setStoreId(d_BookFlow.getStoreId());
                            d_BookerStock.setShopId(d_BookFlow.getShopId());
                            d_BookerStock.setDeviceId(d_BookFlow.getDeviceId());
                            d_BookerStock.setSlotId(d_BookFlow.getSlotId());
                            d_BookerStock.setSkuId(d_BookBorrow.getSkuId());
                            d_BookerStock.setSkuRfId(return_RfId);
                            d_BookerStock.setCreator(operater);
                            d_BookerStock.setCreateTime(CommonUtil.getDateTimeNow());
                            bookerStockMapper.insert(d_BookerStock);
                        }


                        ret_ReturnBooks.add(new BookVo(d_BookBorrow.getSkuId(), d_BookBorrow.getSkuRfId(), d_BookBorrow.getSkuName(), d_BookBorrow.getSkuCumCode(), d_BookBorrow.getSkuImgUrl()));

                    }

                }

                ret.setBorrowBooks(ret_BorrowBooks);
                ret.setReturnBooks(ret_ReturnBooks);

                d_BookFlow.setStatus(4000);
            }

            if(rop.getActionSn()>d_BookFlow.getLastActionSn()) {
                d_BookFlow.setLastActionSn(rop.getActionSn());
                d_BookFlow.setLastActionCode(rop.getActionCode());
                d_BookFlow.setLastActionTime(CommonUtil.toDateTimeTimestamp(rop.getActionTime()));
                d_BookFlow.setLastActionRemark(rop.getActionRemark());
            }

            d_BookFlow.setMendTime(CommonUtil.getDateTimeNow());
            d_BookFlow.setMender(IdWork.buildGuId());

            bookFlowMapper.update(d_BookFlow);

            return result.success("????????????", ret);
        } catch (Exception ex) {
            logger.error("????????????????????????", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result.fail("????????????[99]");
        }
    }

    @Override
    public CustomResult<RetBookerSawBorrowBooks> sawBorrowBooks(String operater, RopBookerSawBorrowBooks rop) {

        CustomResult<RetBookerSawBorrowBooks> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        Page<?> page = PageHelper.startPage(pageNum, pageSize, "CreateTime Desc");

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("ClientUserId", rop.getClientUserId());
        selective.addWhere("Status", "1000");
        List<BookBorrow> d_BookBorrows = bookBorrowMapper.find(selective);

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

            LumosSelective selective= new LumosSelective();
            selective.setFields("*");
            selective.addWhere("ClientUserId", rop.getClientUserId());
            selective.addWhere("Status", "1000");
            selective.addWhere("CanRenew", "1");

            if (actionCode.equals("multi")) {
                selective.addWhere("BorrowIds", rop.getBorrowIds());
            } else if (actionCode.equals("all")) {

            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result.fail("????????????[01]");
            }

            d_BookBorrows = bookBorrowMapper.find(selective);

            if (d_BookBorrows == null || d_BookBorrows.size() <= 0) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return result.fail("????????????[02]???????????????????????????");
            }

            selective= new LumosSelective();
            selective.setFields("*");
            selective.addWhere("ClientUserId", rop.getClientUserId());
            selective.addWhere("IcCardId", rop.getIdentityId());
            IcCard d_IcCard = icCardMapper.findOne(selective);

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
                        return result.fail("????????????[03]");
                    }
                }
            }

            logger.info("????????????");

            return result.success("????????????");
        } catch (Exception ex) {
            logger.error("????????????????????????", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result.fail("????????????[99]");
        }
    }

    @Override
    public CustomResult<RetBookerDisplayBooks> displayBooks(String operater, RopBookerDisplayBooks rop) {

        CustomResult<RetBookerDisplayBooks> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        MerchDeviceVw d_MerchDevice = getDevice(rop.getDeviceId());

        Page<?> page = PageHelper.startPage(pageNum, pageSize, "SkuId Desc");

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId", d_MerchDevice.getMerchId());
        selective.addWhere("StoreId", d_MerchDevice.getStoreId());
        selective.addWhere("ShopId", d_MerchDevice.getShopId());
        selective.addWhere("DeviceId", rop.getDeviceId());

        List<BookerDeviceSkuStockVw> d_BookerDeviceSkuStocksVw = bookerStockMapper.findDevcieSkuStock(selective);

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

        if(CommonUtil.isEmpty(rop.getFlowId())){
            return result.fail(2001, "????????????[D00]");
        }

        new Thread(() -> {
            addFlowLog(rop.getMsgId(), rop.getMsgMode(), rop.getDeviceId(), rop.getFlowId(),rop.getActionSn(), rop.getActionCode(), rop.getActionData(), "", rop.getActionRemark(), rop.getActionTime());
        }).start();

        RetBookerTakeStock ret = new RetBookerTakeStock();
        ret.setFlowId(rop.getFlowId());
        ret.setActionCode(rop.getActionCode());
        try {

            String actionCode = rop.getActionCode();
            String flowId = rop.getFlowId();

            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("FlowId", flowId);

            BookFlow d_BookFlow = bookFlowMapper.findOne(selective);

            if (d_BookFlow == null) {
                return result.fail(2001, "????????????[D01]");
            }

            if (actionCode.equals("rfreader_success")) {

                BookerBorrowReturnActionData actionData = JsonUtil.toObject(rop.getActionData(), new TypeReference<BookerBorrowReturnActionData>() {
                });

                List<String> closeRfIds = actionData.getCloseRfIds();

                BookerTakeStockSheet d_BookerTakeStockSheet = new BookerTakeStockSheet();
                d_BookerTakeStockSheet.setId(IdWork.buildGuId());
                d_BookerTakeStockSheet.setFlowId(rop.getFlowId());
                d_BookerTakeStockSheet.setMerchId(d_BookFlow.getMerchId());
                d_BookerTakeStockSheet.setStoreId(d_BookFlow.getStoreId());
                d_BookerTakeStockSheet.setShopId(d_BookFlow.getShopId());
                d_BookerTakeStockSheet.setDeviceId(d_BookFlow.getDeviceId());
                d_BookerTakeStockSheet.setSlotId(d_BookFlow.getSlotId());

                bookerTakeStockSheetMapper.insert(d_BookerTakeStockSheet);

                List<BookVo> sheetItems = new ArrayList<>();
                List<BookVo> warnItems = new ArrayList<>();

                List<BookerTakeStockSheetItem> d_BookerTakeStockSheetItems = new ArrayList<>();

                for (String closeRfId : closeRfIds) {

                    SkuInfo r_Sku = cacheFactory.getProduct().getSkuInfoByRfId(d_BookerTakeStockSheet.getMerchId(), closeRfId);
                    if (r_Sku != null && !CommonUtil.isEmpty(r_Sku.getId())) {
                        BookerTakeStockSheetItem d_BookerTakeStockSheetItem = new BookerTakeStockSheetItem();
                        d_BookerTakeStockSheetItem.setId(IdWork.buildGuId());
                        d_BookerTakeStockSheetItem.setFlowId(d_BookerTakeStockSheet.getFlowId());
                        d_BookerTakeStockSheetItem.setSheetId(d_BookerTakeStockSheet.getId());
                        d_BookerTakeStockSheetItem.setMerchId(d_BookerTakeStockSheet.getMerchId());
                        d_BookerTakeStockSheetItem.setStoreId(d_BookerTakeStockSheet.getStoreId());
                        d_BookerTakeStockSheetItem.setShopId(d_BookerTakeStockSheet.getShopId());
                        d_BookerTakeStockSheetItem.setDeviceId(d_BookerTakeStockSheet.getDeviceId());
                        d_BookerTakeStockSheetItem.setSlotId(d_BookerTakeStockSheet.getSlotId());
                        d_BookerTakeStockSheetItem.setSkuId(r_Sku.getId());
                        d_BookerTakeStockSheetItem.setSkuRfId(closeRfId);
                        d_BookerTakeStockSheetItem.setCreateTime(d_BookerTakeStockSheet.getCreateTime());
                        d_BookerTakeStockSheetItem.setCreator(d_BookerTakeStockSheet.getCreator());

                        d_BookerTakeStockSheetItems.add(d_BookerTakeStockSheetItem);

                        sheetItems.add(new BookVo(r_Sku.getId(), closeRfId, r_Sku.getName(), r_Sku.getCumCode(), r_Sku.getCumCode()));
                    }
                    else {
                        warnItems.add(new BookVo("0", closeRfId, "??????", "0", "0"));
                    }
                }

                d_BookerTakeStockSheet.setQuantity(sheetItems.size());
                d_BookerTakeStockSheet.setCreateTime(CommonUtil.getDateTimeNow());
                d_BookerTakeStockSheet.setCreator(operater);

                if(d_BookerTakeStockSheetItems.size()>0) {
                    bookerTakeStockSheetItemMapper.insertBatch(d_BookerTakeStockSheetItems);
                }

                d_BookFlow.setCloseRfIds(JsonUtil.getJson(closeRfIds));
                d_BookFlow.setStatus(4000);

                ret.setSheetId(d_BookerTakeStockSheet.getId());
                ret.setSheetItems(sheetItems);
                ret.setWarnItems(warnItems);
                ret.setSheetIsUse(false);
            }

            if(rop.getActionSn()>=d_BookFlow.getLastActionSn()) {
                d_BookFlow.setLastActionSn(rop.getActionSn());
                d_BookFlow.setLastActionCode(rop.getActionCode());
                d_BookFlow.setLastActionTime(CommonUtil.toDateTimeTimestamp(rop.getActionTime()));
                d_BookFlow.setLastActionRemark(rop.getActionRemark());
            }

            d_BookFlow.setMendTime(CommonUtil.getDateTimeNow());
            d_BookFlow.setMender(IdWork.buildGuId());

            bookFlowMapper.update(d_BookFlow);

            return result.success("????????????",ret);

        } catch (Exception ex) {
            logger.error("????????????????????????", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result.fail("????????????[99]");
        }
    }

    @Override
    public  CustomResult<RetBookerStockSlots> stockSlots(String operater, RopBookerStockSlots rop) {

        CustomResult<RetBookerStockSlots> result = new CustomResult<>();

        MerchDeviceVw d_MerchDevice = getDevice(rop.getDeviceId());

        if (d_MerchDevice == null)
            return result.fail("????????????[D01]");

        if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
            return result.fail("????????????[D02]");

        if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
            return result.fail("????????????[D03]");

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        long deviceStockQuantity=bookerStockMapper.getDeviceStockQuantityByDeviceId(d_MerchDevice.getMerchId(),d_MerchDevice.getStoreId(),d_MerchDevice.getShopId(),d_MerchDevice.getId());

        Page<?> page = PageHelper.startPage(pageNum, pageSize, "SlotId");

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("DeviceId", rop.getDeviceId());

        List<BookerSlot> d_BookerSlots = bookerSlotMapper.find(selective);

        List<Object> items = new ArrayList<>();

        for (BookerSlot d_BookerSlot : d_BookerSlots) {

            HashMap<String, Object> item = new HashMap<>();

            item.put("slotId", d_BookerSlot.getSlotId());
            item.put("name", d_BookerSlot.getName());
            item.put("lockeqId", d_BookerSlot.getLockeqId());
            item.put("lockeqAnt", d_BookerSlot.getLockeqAnt());
            item.put("rfeqId", d_BookerSlot.getRfeqId());
            item.put("rfeqAnt", d_BookerSlot.getRfeqAnt());
            item.put("isOpen", false);
            item.put("lastInboundTime", CommonUtil.toDateTimeStr(d_BookerSlot.getLastInboundTime()));

            long stockQuantity = bookerStockMapper.getDeviceStockQuantityBySlotId(d_MerchDevice.getMerchId(), d_MerchDevice.getStoreId(), d_MerchDevice.getShopId(), rop.getDeviceId(),d_BookerSlot.getSlotId());

            item.put("stockQuantity", stockQuantity);

            items.add(item);
        }

        long total = page.getTotal();
        RetBookerStockSlots ret = new RetBookerStockSlots();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);
        ret.setStockQuantity(deviceStockQuantity);

        return result.success("????????????", ret);
    }

    @Override
    @Transactional
    public  CustomResult<RetBookerStockInbound> stockInbound(String operater, RopBookerStockInbound rop) {

        CustomResult<RetBookerStockInbound> result = new CustomResult<>();

        try {

            MerchDeviceVw d_MerchDevice = getDevice(rop.getDeviceId());

            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("MerchId", d_MerchDevice.getMerchId());
            selective.addWhere("StoreId", d_MerchDevice.getStoreId());
            selective.addWhere("ShopId", d_MerchDevice.getShopId());
            selective.addWhere("DeviceId", rop.getDeviceId());
            selective.addWhere("SheetId", rop.getSheetId());

            BookerTakeStockSheet d_BookerTakeStockSheet=bookerTakeStockSheetMapper.findOne(selective);

            if(d_BookerTakeStockSheet==null)
                return result.fail("??????????????????[01]");

            if(d_BookerTakeStockSheet.getIsUse()) {
                return result.fail("??????????????????[02]");
            }

            bookerStockMapper.deleteDeviceStockBySlotId(d_MerchDevice.getMerchId(), d_MerchDevice.getStoreId(), d_MerchDevice.getShopId(), rop.getDeviceId(),d_BookerTakeStockSheet.getSlotId());

            selective = new LumosSelective();

            selective.setFields("*");
            selective.addWhere("MerchId", d_MerchDevice.getMerchId());
            selective.addWhere("StoreId", d_MerchDevice.getStoreId());
            selective.addWhere("ShopId", d_MerchDevice.getShopId());
            selective.addWhere("DeviceId", rop.getDeviceId());
            selective.addWhere("SheetId", rop.getSheetId());

            List<BookerTakeStockSheetItem> d_BookerTakeStockSheetItems =bookerTakeStockSheetItemMapper.find(selective);

            List<BookerStock> d_BookerStocks = new ArrayList<>();

            for (BookerTakeStockSheetItem d_BookerTakeStockSheetItem : d_BookerTakeStockSheetItems) {

                BookerStock d_BookerStock = new BookerStock();
                d_BookerStock.setId(IdWork.buildGuId());
                d_BookerStock.setMerchId(d_MerchDevice.getMerchId());
                d_BookerStock.setStoreId(d_MerchDevice.getStoreId());
                d_BookerStock.setShopId(d_MerchDevice.getShopId());
                d_BookerStock.setDeviceId(rop.getDeviceId());
                d_BookerStock.setSlotId(d_BookerTakeStockSheetItem.getSlotId());
                d_BookerStock.setSkuRfId(d_BookerTakeStockSheetItem.getSkuRfId());
                d_BookerStock.setSkuId(d_BookerTakeStockSheetItem.getSkuId());
                d_BookerStock.setCreateTime(CommonUtil.getDateTimeNow());
                d_BookerStock.setCreator(operater);

                d_BookerStocks.add(d_BookerStock);
            }

            d_BookerTakeStockSheet.setIsUse(true);

            bookerStockMapper.insertBatch(d_BookerStocks);

            bookerTakeStockSheetMapper.update(d_BookerTakeStockSheet);

            BookerSlot bookerSlot=new BookerSlot();
            bookerSlot.setDeviceId(rop.getDeviceId());
            bookerSlot.setSlotId(d_BookerTakeStockSheet.getSlotId());
            bookerSlot.setLastInboundSheetId(d_BookerTakeStockSheet.getId());
            bookerSlot.setLastInboundTime(CommonUtil.getDateTimeNow());
            bookerSlot.setMender(operater);
            bookerSlot.setMendTime(CommonUtil.getDateTimeNow());

            bookerSlotMapper.update(bookerSlot);

            return result.success("??????????????????[99]");
        }
        catch (Exception ex){
            logger.error("????????????????????????", ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result.fail("??????????????????[99]");
        }
    }

    private void addFlowLog(String msgId, String msgMode, String deviceId, String flowId,int actionSn, String actionCode, String actionData, String actionResult, String actionRemark, String actionTime) {

        String merchId = null;
        String deviceCumCode = null;

        MerchDeviceVw d_MerchDevice = getDevice(deviceId);
        if (d_MerchDevice != null) {
            merchId = d_MerchDevice.getMerchId();
            deviceCumCode = d_MerchDevice.getCumCode();
        }

        LumosSelective selective = new LumosSelective();
        selective.addWhere("MerchId", merchId);
        selective.addWhere("DeviceId", deviceId);
        selective.addWhere("FlowId", flowId);
        selective.addWhere("ActionSn",String.valueOf(actionSn));
        BookFlowLog d_BookFlowLog= bookFlowLogMapper.findOne(selective);

        if(d_BookFlowLog==null) {
            d_BookFlowLog = new BookFlowLog();
            d_BookFlowLog.setId(IdWork.buildLongId());
            d_BookFlowLog.setMsgId(msgId);
            d_BookFlowLog.setMsgMode(msgMode);
            d_BookFlowLog.setMerchId(merchId);
            d_BookFlowLog.setDeviceId(deviceId);
            d_BookFlowLog.setDeviceCumCode(deviceCumCode);
            d_BookFlowLog.setFlowId(flowId);
            d_BookFlowLog.setActionSn(actionSn);
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