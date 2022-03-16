package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.rop.model.ActionDataByOpenRequest;
import com.caterbao.lumos.api.device.rop.model.BookBean;
import com.caterbao.lumos.api.device.service.BookerService;
import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.*;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlow;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlowData;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowFlowLog;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BookerServiceImpl implements BookerService {

    private SysUserMapper sysUserMapper;
    private IcCardMapper icCardMapper;
    private BookBorrowFlowMapper bookBorrowFlowMapper;
    private BookBorrowFlowDataMapper bookBorrowFlowDataMapper;
    private BookBorrowFlowLogMapper bookBorrowFlowLogMapper;
    private MerchDeviceMapper merchDeviceMapper;
    private PlatformTransactionManager platformTransactionManager;
    private TransactionDefinition transactionDefinition;
    private CacheFactory cacheFactory;

    
    private final Lock lock = new ReentrantLock();

    @Autowired(required = false)
    public void setBookBorrowFlowMapper(BookBorrowFlowMapper bookBorrowFlowMapper) {
        this.bookBorrowFlowMapper = bookBorrowFlowMapper;
    }

    @Autowired(required = false)
    public void setBookBorrowFlowDataMapper(BookBorrowFlowDataMapper bookBorrowFlowDataMapper) {
        this.bookBorrowFlowDataMapper = bookBorrowFlowDataMapper;
    }

    @Autowired(required = false)
    public void setBookBorrowFlowLogMapper(BookBorrowFlowLogMapper bookBorrowFlowLogMapper) {
        this.bookBorrowFlowLogMapper = bookBorrowFlowLogMapper;
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
    public CustomResult<RetBookerBorrowReturn> borrowReturn(String operater, RopBookerBorrowReturn rop) {
        CustomResult<RetBookerBorrowReturn> result = new CustomResult<>();

        new Thread(() -> {
            addBorrowReturnFlowLog(rop.getTrgId(), rop.getDeviceId(), rop.getFlowId(), rop.getActionCode(), rop.getActionData(), "", rop.getActionRemark(), rop.getActionTime());
        }).start();

        if (rop.getActionCode().equals("create_flow")) {
            return borrowReturnCreateFlow(rop.getDeviceId(), rop.getCabinetId(), rop.getSlotId(), rop.getClientUserId(), rop.getIdentityType(), rop.getIdentityId());
        } else if (rop.getActionCode().equals("request_open_auth")) {
            ActionDataByOpenRequest actionData = JsonUtil.toObject(rop.getActionData(), new TypeReference<ActionDataByOpenRequest>() {
            });
            return borrowReturnRequestOpenAuth(rop.getFlowId(), actionData.getRfIds());
        } else if (rop.getActionCode().equals("request_close_auth")) {
            ActionDataByOpenRequest actionData = JsonUtil.toObject(rop.getActionData(), new TypeReference<ActionDataByOpenRequest>() {
            });
            return borrowReturnRequestCloseAuth(rop.getFlowId(), actionData.getRfIds());
        } else {
            RetBookerBorrowReturn ret = new RetBookerBorrowReturn();
            ret.setFlowId(rop.getFlowId());
            return result.success("", ret);
        }
    }

    private CustomResult<RetBookerBorrowReturn> borrowReturnCreateFlow(String deviceId,
                                                                       String cabinetId,
                                                                       String slotId,
                                                                       String clientUserId,
                                                                       int identityType,
                                                                       String identityId) {

        CustomResult<RetBookerBorrowReturn> result = new CustomResult<>();


        if (CommonUtil.isEmpty(deviceId))
            return result.fail("创建失败[01]");

        if (CommonUtil.isEmpty(cabinetId))
            return result.fail("创建失败[02]");

        if (CommonUtil.isEmpty(slotId))
            return result.fail("创建失败[03]");

        if (CommonUtil.isEmpty(clientUserId))
            return result.fail("创建失败[04]");

        if (CommonUtil.isEmpty(identityType))
            return result.fail("创建失败[05]");

        if (CommonUtil.isEmpty(identityId))
            return result.fail("创建失败[06]");


        LumosSelective selective_MerchDevice = new LumosSelective();
        selective_MerchDevice.addWhere("DeviceId", deviceId);
        selective_MerchDevice.addWhere("BindStatus", "1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);

        if (d_MerchDevice == null)
            return result.fail("创建失败[07]");

        if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
            return result.fail("创建失败[08]");

        if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
            return result.fail("创建失败[09]");

        BookBorrowFlow d_BookBorrowFlow = new BookBorrowFlow();

        d_BookBorrowFlow.setId(IdWork.buildLongId());
        d_BookBorrowFlow.setMerchId(d_MerchDevice.getMerchId());
        d_BookBorrowFlow.setMerchName(d_MerchDevice.getMerchName());
        d_BookBorrowFlow.setStoreId(d_MerchDevice.getStoreId());
        d_BookBorrowFlow.setStoreName(d_MerchDevice.getStoreName());
        d_BookBorrowFlow.setShopId(d_MerchDevice.getShopId());
        d_BookBorrowFlow.setShopName(d_MerchDevice.getShopName());
        d_BookBorrowFlow.setDeviceId(deviceId);
        d_BookBorrowFlow.setDeviceCumCode(d_MerchDevice.getCumCode());
        d_BookBorrowFlow.setCabinetId(cabinetId);
        d_BookBorrowFlow.setSlotId(slotId);
        d_BookBorrowFlow.setClientUserId(clientUserId);
        d_BookBorrowFlow.setIdentityType(identityType);
        d_BookBorrowFlow.setIdentityId(identityId);
        d_BookBorrowFlow.setIdentityName(getIdentityName(identityType, identityId));
        d_BookBorrowFlow.setStatus(1);
        d_BookBorrowFlow.setCreateTime(CommonUtil.getDateTimeNow());
        d_BookBorrowFlow.setCreator(IdWork.buildGuId());

        if (bookBorrowFlowMapper.insert(d_BookBorrowFlow) <= 0)
            return result.fail("创建失败[10]");

        RetBookerBorrowReturn ret = new RetBookerBorrowReturn();
        ret.setFlowId(d_BookBorrowFlow.getId());

        return result.success("创建成功", ret);

    }

    private CustomResult<RetBookerBorrowReturn> borrowReturnRequestOpenAuth(String flowId,List<String> openRfIds) {

        CustomResult<RetBookerBorrowReturn> result = new CustomResult<>();


        LumosSelective selective_BookBorrowFlow = new LumosSelective();
        selective_BookBorrowFlow.setFields("*");
        selective_BookBorrowFlow.addWhere("FlowId", flowId);


        BookBorrowFlow d_BookBorrowFlow = bookBorrowFlowMapper.findOne(selective_BookBorrowFlow);

        if (d_BookBorrowFlow == null)
            return result.fail("验证失败[01]");

        d_BookBorrowFlow.setOpenActionTime(CommonUtil.getDateTimeNow());
        d_BookBorrowFlow.setOpenRfIds(JsonUtil.getJson(openRfIds));
        d_BookBorrowFlow.setStatus(1);
        d_BookBorrowFlow.setMendTime(CommonUtil.getDateTimeNow());
        d_BookBorrowFlow.setMender(IdWork.buildGuId());

        if (bookBorrowFlowMapper.update(d_BookBorrowFlow) <= 0)
            return result.fail("验证失败[02]");

        RetBookerBorrowReturn ret = new RetBookerBorrowReturn();
        ret.setFlowId(d_BookBorrowFlow.getId());

        return result.success("验证成功", ret);
    }

    private CustomResult<RetBookerBorrowReturn> borrowReturnRequestCloseAuth(String flowId, List<String> closeRfIds){

        CustomResult<RetBookerBorrowReturn> result=new CustomResult<>();

        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {

            List<BookBean> ret_BorrowBooks = new ArrayList<>();
            List<BookBean> ret_ReturnBooks = new ArrayList<>();

            LumosSelective selective_BookBorrowFlow = new LumosSelective();
            selective_BookBorrowFlow.setFields("*");
            selective_BookBorrowFlow.addWhere("FlowId", flowId);

            //查找借还流程
            BookBorrowFlow d_BookBorrowFlow = bookBorrowFlowMapper.findOne(selective_BookBorrowFlow);
            if (d_BookBorrowFlow == null) {
                lock.unlock();
                return result.fail("验证失败[01]");
            }

            d_BookBorrowFlow.setCloseActionTime(CommonUtil.getDateTimeNow());
            d_BookBorrowFlow.setCloseRfIds(JsonUtil.getJson(closeRfIds));
            d_BookBorrowFlow.setMender(IdWork.buildGuId());
            d_BookBorrowFlow.setMendTime(CommonUtil.getDateTimeNow());

            if (bookBorrowFlowMapper.update(d_BookBorrowFlow) <= 0) {
                lock.unlock();
                return result.fail("验证失败[02]");
            }

            List<String> open_RfIds = JsonUtil.toObject(d_BookBorrowFlow.getOpenRfIds(),new TypeReference<List<String>>() {});

            if(open_RfIds==null) {
                open_RfIds = new ArrayList<>();
            }

            List<String> close_RfIds =JsonUtil.toObject(d_BookBorrowFlow.getCloseRfIds(),new TypeReference<List<String>>() {});
            if (close_RfIds == null) {
                close_RfIds = new ArrayList<>();
            }

            List<String> borrow_RfIds = new ArrayList<>();

            if(open_RfIds!=null) {
                for (String open_rfId : open_RfIds) {
                    if (!close_RfIds.contains(open_rfId)) {
                        borrow_RfIds.add(open_rfId);
                    }
                }
            }

            List<String> return_RfIds = new ArrayList<>();

            if(close_RfIds!=null) {
                for (String close_rfId : close_RfIds) {
                    if (!open_RfIds.contains(close_rfId)) {
                        return_RfIds.add(close_rfId);
                    }
                }
            }

            int expireDay=30;
            //借书
            for (int i=0;i< borrow_RfIds.size() ; i++) {

                String borrow_RfId=borrow_RfIds.get(i);

                SkuInfo r_Sku = cacheFactory.getProduct().getSkuInfoByRfId(d_BookBorrowFlow.getMerchId(), borrow_RfId);

                LumosSelective selective_BookBorrowReturnFlowBook = new LumosSelective();
                selective_BookBorrowReturnFlowBook.setFields("*");
                selective_BookBorrowReturnFlowBook.addWhere("MerchId", d_BookBorrowFlow.getMerchId());
                selective_BookBorrowReturnFlowBook.addWhere("FlowId",flowId);
                selective_BookBorrowReturnFlowBook.addWhere("SkuRfId", borrow_RfId);

                BookBorrowFlowData d_BookBorrowFlowData = bookBorrowFlowDataMapper.findOne(selective_BookBorrowReturnFlowBook);
                if (d_BookBorrowFlowData == null) {
                    d_BookBorrowFlowData = new BookBorrowFlowData();
                    d_BookBorrowFlowData.setId(String.valueOf(d_BookBorrowFlow.getId())+String.valueOf(i));
                    d_BookBorrowFlowData.setMerchId(d_BookBorrowFlow.getMerchId());
                    d_BookBorrowFlowData.setMerchName(d_BookBorrowFlow.getMerchName());
                    d_BookBorrowFlowData.setStoreId(d_BookBorrowFlow.getStoreId());
                    d_BookBorrowFlowData.setStoreName(d_BookBorrowFlow.getStoreName());
                    d_BookBorrowFlowData.setShopId(d_BookBorrowFlow.getShopId());
                    d_BookBorrowFlowData.setShopName(d_BookBorrowFlow.getShopName());
                    d_BookBorrowFlowData.setDeviceId(d_BookBorrowFlow.getDeviceId());
                    d_BookBorrowFlowData.setDeviceCumCode(d_BookBorrowFlow.getDeviceCumCode());
                    d_BookBorrowFlowData.setCabinetId(d_BookBorrowFlow.getCabinetId());
                    d_BookBorrowFlowData.setSlotId(d_BookBorrowFlow.getSlotId());
                    d_BookBorrowFlowData.setFlowId(d_BookBorrowFlow.getId());
                    d_BookBorrowFlowData.setIdentityType(d_BookBorrowFlow.getIdentityType());
                    d_BookBorrowFlowData.setIdentityId(d_BookBorrowFlow.getIdentityId());
                    d_BookBorrowFlowData.setIdentityName(d_BookBorrowFlow.getIdentityName());
                    d_BookBorrowFlowData.setClientUserId(d_BookBorrowFlow.getClientUserId());
                    d_BookBorrowFlowData.setSkuRfId(borrow_RfId);
                    d_BookBorrowFlowData.setSkuId(r_Sku.getId());
                    d_BookBorrowFlowData.setSkuName(r_Sku.getName());
                    d_BookBorrowFlowData.setSkuCumCode(r_Sku.getCumCode());
                    d_BookBorrowFlowData.setSkuImgUrl(r_Sku.getImgUrl());
                    d_BookBorrowFlowData.setBorrowSeq(i);
                    d_BookBorrowFlowData.setBorrowWay(1);
                    d_BookBorrowFlowData.setBorrowTime(CommonUtil.getDateTimeNow());
                    d_BookBorrowFlowData.setBorrowStatus(1);
                    d_BookBorrowFlowData.setExpireTime(CommonUtil.getDateTimeNowAndAddDay(expireDay));
                    d_BookBorrowFlowData.setCreator(IdWork.buildGuId());
                    d_BookBorrowFlowData.setCreateTime(CommonUtil.getDateTimeNow());

                    if (bookBorrowFlowDataMapper.insert(d_BookBorrowFlowData) <= 0) {
                        lock.unlock();
                        return result.fail("验证失败[03]");
                    }
                    else {
                        ret_BorrowBooks.add(new BookBean(d_BookBorrowFlowData.getSkuId(),d_BookBorrowFlowData.getSkuRfId(),d_BookBorrowFlowData.getSkuName(),d_BookBorrowFlowData.getDeviceCumCode(),d_BookBorrowFlowData.getSkuImgUrl()));
                    }
                }
            }

            //还书
            for (String return_RfId : return_RfIds) {

                LumosSelective selective_BookBorrowReturnFlowBook = new LumosSelective();
                selective_BookBorrowReturnFlowBook.setFields("*");
                selective_BookBorrowReturnFlowBook.addWhere("MerchId", d_BookBorrowFlow.getMerchId());
                selective_BookBorrowReturnFlowBook.addWhere("SkuRfId", return_RfId);
                BookBorrowFlowData d_BookBorrowFlowData = bookBorrowFlowDataMapper.findOne(selective_BookBorrowReturnFlowBook);
                if(d_BookBorrowFlowData !=null) {

                    d_BookBorrowFlowData.setReturnWay(1);
                    d_BookBorrowFlowData.setReturnFlowId(flowId);
                    d_BookBorrowFlowData.setReturnTime(CommonUtil.getDateTimeNow());
                    d_BookBorrowFlowData.setMender(IdWork.buildGuId());
                    d_BookBorrowFlowData.setMendTime(CommonUtil.getDateTimeNow());

                    if (bookBorrowFlowDataMapper.update(d_BookBorrowFlowData) <= 0) {
                        lock.unlock();
                        return result.fail("验证失败[04]");
                    }
                    else
                    {
                        ret_ReturnBooks.add(new BookBean(d_BookBorrowFlowData.getSkuId(),d_BookBorrowFlowData.getSkuRfId(),d_BookBorrowFlowData.getSkuName(),d_BookBorrowFlowData.getDeviceCumCode(),d_BookBorrowFlowData.getSkuImgUrl()));
                    }
                }
            }

            RetBookerBorrowReturn ret = new RetBookerBorrowReturn();
            ret.setFlowId(flowId);
            ret.setBorrowBooks(ret_BorrowBooks);
            ret.setReturnBooks(ret_ReturnBooks);


            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  result.success("验证成功",ret);
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return result.fail("验证失败[99]");
        }

    }

    private void  addBorrowReturnFlowLog(String trgId,String deviceId,String flowId,String actionCode,String actionData,String actionResult,String actionRemark, String actionTime ) {

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

        BookBorrowFlowLog d_BookBorrowFlowLog = new BookBorrowFlowLog();
        d_BookBorrowFlowLog.setId(IdWork.buildLongId());
        d_BookBorrowFlowLog.setTrgId(trgId);
        d_BookBorrowFlowLog.setMerchId(merchId);
        d_BookBorrowFlowLog.setDeviceId(deviceId);
        d_BookBorrowFlowLog.setDeviceCumCode(deviceCumCode);
        d_BookBorrowFlowLog.setFlowId(flowId);
        d_BookBorrowFlowLog.setActionCode(actionCode);
        d_BookBorrowFlowLog.setActionData(actionData);
        d_BookBorrowFlowLog.setActionResult(actionResult);
        d_BookBorrowFlowLog.setActionRemark(actionRemark);
        d_BookBorrowFlowLog.setActionTime(CommonUtil.toDateTimeTimestamp(actionTime));
        d_BookBorrowFlowLog.setCreator(IdWork.buildGuId());
        d_BookBorrowFlowLog.setCreateTime(CommonUtil.getDateTimeNow());
        bookBorrowFlowLogMapper.insert(d_BookBorrowFlowLog);
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