package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.rop.vo.ActionDataByOpenRequest;
import com.caterbao.lumos.api.device.rop.vo.BookVo;
import com.caterbao.lumos.api.device.service.BookerService;
import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.*;
import com.caterbao.lumos.locals.dal.pojo.BookFlow;
import com.caterbao.lumos.locals.dal.pojo.BookBorrow;
import com.caterbao.lumos.locals.dal.pojo.BookFlowLog;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BookerServiceImpl implements BookerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SysUserMapper sysUserMapper;
    private IcCardMapper icCardMapper;
    private BookFlowMapper bookFlowMapper;
    private BookBorrowMapper bookBorrowMapper;
    private BookFlowLogMapper bookFlowLogMapper;
    private MerchDeviceMapper merchDeviceMapper;
    private PlatformTransactionManager platformTransactionManager;
    private TransactionDefinition transactionDefinition;
    private CacheFactory cacheFactory;

    
    private final Lock lock = new ReentrantLock();

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
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Autowired(required = false)
    public void setIcCardMapper(IcCardMapper icCardMapper) {
        this.icCardMapper = icCardMapper;
    }

    @Autowired
    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Autowired
    public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
        this.transactionDefinition = transactionDefinition;
    }

    @Autowired
    public void setCacheFactory(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    @Override
    public CustomResult<RetBookerCreateFlow> createFlow(String operater,RopBookerCreateFlow rop) {
        logger.info("createFlow");

        CustomResult<RetBookerCreateFlow> result = new CustomResult<>();

        if (CommonUtil.isEmpty(rop.getDeviceId()))
            return result.fail("创建失败[W01]");

        if (CommonUtil.isEmpty(rop.getSlotId()))
            return result.fail("创建失败[W03]");

        if (CommonUtil.isEmpty(rop.getClientUserId()))
            return result.fail("创建失败[W04]");

        if (CommonUtil.isEmpty(rop.getIdentityType()))
            return result.fail("创建失败[W05]");

        if (CommonUtil.isEmpty(rop.getIdentityId()))
            return result.fail("创建失败[W06]");

        if(rop.getType()==0)
            return result.fail("创建失败[W07]");

        LumosSelective selective_MerchDevice = new LumosSelective();
        selective_MerchDevice.addWhere("DeviceId", rop.getDeviceId());
        selective_MerchDevice.addWhere("BindStatus", "1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);

        if (d_MerchDevice == null)
            return result.fail("创建失败[D01]");

        if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
            return result.fail("创建失败[D02]");

        if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
            return result.fail("创建失败[D03]");



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
        d_BookFlow.setIdentityName(getIdentityName(rop.getIdentityType(), rop.getIdentityId()));
        d_BookFlow.setStatus(1);
        d_BookFlow.setCreateTime(CommonUtil.getDateTimeNow());
        d_BookFlow.setCreator(IdWork.buildGuId());

        if (bookFlowMapper.insert(d_BookFlow) <= 0)
            return result.fail("创建失败[D04]");

        RetBookerCreateFlow ret = new RetBookerCreateFlow();
        ret.setFlowId(d_BookFlow.getId());

        return result.success("创建成功", ret);

    }

    @Override
    public CustomResult<RetBookerBorrowReturn> borrowReturn(String operater, RopBookerBorrowReturn rop) {
        logger.info("borrowReturn");
        CustomResult<RetBookerBorrowReturn> result = new CustomResult<>();

        new Thread(() -> {
            addBorrowReturnFlowLog(rop.getDeviceId(), rop.getFlowId(), rop.getActionCode(), rop.getActionData(), "", rop.getActionRemark(), rop.getActionTime());
        }).start();

        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {

            RetBookerBorrowReturn ret = new RetBookerBorrowReturn();

            String actionCode = rop.getActionCode();
            String flowId = rop.getFlowId();

            LumosSelective selective_BookFlow = new LumosSelective();
            selective_BookFlow.setFields("*");
            selective_BookFlow.addWhere("FlowId", flowId);

            BookFlow d_BookFlow = bookFlowMapper.findOne(selective_BookFlow);

            if (d_BookFlow == null) {
                lock.unlock();
                return result.fail("验证失败[D01]");
            }

            if (actionCode.equals("request_open_auth"))
            {

                ActionDataByOpenRequest actionData = JsonUtil.toObject(rop.getActionData(), new TypeReference<ActionDataByOpenRequest>() {
                });

                List<String> open_RfIds = actionData.getRfIds();

                d_BookFlow.setOpenActionTime(CommonUtil.getDateTimeNow());
                d_BookFlow.setOpenRfIds(JsonUtil.getJson(open_RfIds));

            }
            else if (actionCode.equals("request_close_auth")) {

                ActionDataByOpenRequest actionData = JsonUtil.toObject(rop.getActionData(), new TypeReference<ActionDataByOpenRequest>() {
                });

                List<String> close_RfIds = actionData.getRfIds();

                List<BookVo> ret_BorrowBooks = new ArrayList<>();
                List<BookVo> ret_ReturnBooks = new ArrayList<>();

                d_BookFlow.setCloseActionTime(CommonUtil.getDateTimeNow());
                d_BookFlow.setCloseRfIds(JsonUtil.getJson(close_RfIds));

                List<String> open_RfIds = JsonUtil.toObject(d_BookFlow.getOpenRfIds(), new TypeReference<List<String>>() {
                });

                if (open_RfIds == null) {
                    open_RfIds = new ArrayList<>();
                }

                if (close_RfIds == null) {
                    close_RfIds = new ArrayList<>();
                }

                List<String> borrow_RfIds = new ArrayList<>();

                if (open_RfIds != null) {
                    for (String open_rfId : open_RfIds) {
                        if (!close_RfIds.contains(open_rfId)) {
                            borrow_RfIds.add(open_rfId);
                        }
                    }
                }

                List<String> return_RfIds = new ArrayList<>();

                if (close_RfIds != null) {
                    for (String close_rfId : close_RfIds) {
                        if (!open_RfIds.contains(close_rfId)) {
                            return_RfIds.add(close_rfId);
                        }
                    }
                }

                int expireDay = 30;
                //借书
                for (int i = 0; i < borrow_RfIds.size(); i++) {

                    String borrow_RfId = borrow_RfIds.get(i);

                    SkuInfo r_Sku = cacheFactory.getProduct().getSkuInfoByRfId(d_BookFlow.getMerchId(), borrow_RfId);

                    LumosSelective selective_BookBorrow = new LumosSelective();
                    selective_BookBorrow.setFields("*");
                    selective_BookBorrow.addWhere("MerchId", d_BookFlow.getMerchId());
                    selective_BookBorrow.addWhere("FlowId", flowId);
                    selective_BookBorrow.addWhere("SkuRfId", borrow_RfId);

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
                        d_BookBorrow.setBorrowSeq(i);
                        d_BookBorrow.setBorrowWay(1);
                        d_BookBorrow.setBorrowTime(CommonUtil.getDateTimeNow());
                        d_BookBorrow.setStatus(1000);
                        d_BookBorrow.setRenewCount(0);
                        d_BookBorrow.setExpireTime(CommonUtil.getDateTimeNowAndAddDay(expireDay));
                        d_BookBorrow.setCreator(IdWork.buildGuId());
                        d_BookBorrow.setCreateTime(CommonUtil.getDateTimeNow());

                        if (bookBorrowMapper.insert(d_BookBorrow) <= 0) {
                            lock.unlock();
                            return result.fail("验证失败[D03]");
                        } else {
                            ret_BorrowBooks.add(new BookVo(d_BookBorrow.getSkuId(), d_BookBorrow.getSkuRfId(), d_BookBorrow.getSkuName(), d_BookBorrow.getDeviceCumCode(), d_BookBorrow.getSkuImgUrl()));
                        }
                    }
                }

                //还书
                for (String return_RfId : return_RfIds) {

                    LumosSelective selective_BookBorrow = new LumosSelective();
                    selective_BookBorrow.setFields("*");
                    selective_BookBorrow.addWhere("MerchId", d_BookFlow.getMerchId());
                    selective_BookBorrow.addWhere("SkuRfId", return_RfId);
                    BookBorrow d_BookBorrow = bookBorrowMapper.findOne(selective_BookBorrow);
                    if (d_BookBorrow != null) {
                        d_BookBorrow.setReturnFlowId(flowId);
                        d_BookBorrow.setReturnWay(d_BookBorrow.getBorrowWay());
                        d_BookBorrow.setReturnTime(CommonUtil.getDateTimeNow());
                        d_BookBorrow.setStatus(3000);
                        d_BookBorrow.setMender(IdWork.buildGuId());
                        d_BookBorrow.setMendTime(CommonUtil.getDateTimeNow());

                        if (bookBorrowMapper.update(d_BookBorrow) <= 0) {
                            lock.unlock();
                            return result.fail("验证失败[D04]");
                        } else {
                            ret_ReturnBooks.add(new BookVo(d_BookBorrow.getSkuId(), d_BookBorrow.getSkuRfId(), d_BookBorrow.getSkuName(), d_BookBorrow.getDeviceCumCode(), d_BookBorrow.getSkuImgUrl()));
                        }
                    }
                }

                ret.setFlowId(flowId);
                ret.setBorrowBooks(ret_BorrowBooks);
                ret.setReturnBooks(ret_ReturnBooks);
            }

            d_BookFlow.setLastActionCode(rop.getActionCode());
            d_BookFlow.setLastActionTime(CommonUtil.toDateTimeTimestamp(rop.getActionTime()));
            d_BookFlow.setLastActionRemark(rop.getActionRemark());
            d_BookFlow.setMendTime(CommonUtil.getDateTimeNow());
            d_BookFlow.setMender(IdWork.buildGuId());

            if (bookFlowMapper.update(d_BookFlow) <= 0) {
                lock.unlock();
                return result.fail("验证失败[D02]");
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();

            return result.success("验证成功", ret);
        } catch (Exception ex) {
            logger.error("borrowReturn_Exception", ex);
            platformTransactionManager.rollback(transaction);
            ex.printStackTrace();
            lock.unlock();
            return result.fail("验证失败[99]");
        }
    }

    private void  addBorrowReturnFlowLog(String deviceId,String flowId,String actionCode,String actionData,String actionResult,String actionRemark, String actionTime ) {

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

        BookFlowLog d_BookFlowLog = new BookFlowLog();
        d_BookFlowLog.setId(IdWork.buildLongId());
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

    private String getIdentityName(int type,String id) {
        String name = "";

        if (type == 1) {
            name =icCardMapper.getFullNameById(id);
        } else if (type == 2) {
            name = sysUserMapper.getFullNameById(id);
        }

        return name;
    }

}