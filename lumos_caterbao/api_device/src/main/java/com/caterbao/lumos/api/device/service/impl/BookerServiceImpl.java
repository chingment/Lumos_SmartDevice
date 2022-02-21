package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.rop.model.BookBean;
import com.caterbao.lumos.api.device.service.BookerService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowReturnFlowDataMapper;
import com.caterbao.lumos.locals.dal.mapper.BookBorrowReturnFlowMapper;
import com.caterbao.lumos.locals.dal.mapper.MerchDeviceMapper;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowReturnFlow;
import com.caterbao.lumos.locals.dal.pojo.BookBorrowReturnFlowData;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import com.fasterxml.jackson.core.type.TypeReference;
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

    private BookBorrowReturnFlowMapper bookBorrowReturnFlowMapper;
    private BookBorrowReturnFlowDataMapper bookBorrowReturnFlowDataMapper;
    private MerchDeviceMapper merchDeviceMapper;
    private PlatformTransactionManager platformTransactionManager;
    private TransactionDefinition transactionDefinition;
    private final Lock lock = new ReentrantLock();

    @Autowired(required = false)
    public void setBookBorrowReturnFlowMapper(BookBorrowReturnFlowMapper bookBorrowReturnFlowMapper) {
        this.bookBorrowReturnFlowMapper = bookBorrowReturnFlowMapper;
    }

    @Autowired(required = false)
    public void setBookBorrowReturnFlowDataMapper(BookBorrowReturnFlowDataMapper bookBorrowReturnFlowDataMapper) {
        this.bookBorrowReturnFlowDataMapper = bookBorrowReturnFlowDataMapper;
    }

    @Autowired(required = false)
    public void setMerchDeviceMapper(MerchDeviceMapper merchDeviceMapper) {
        this.merchDeviceMapper = merchDeviceMapper;
    }

    @Autowired
    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Autowired
    public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
        this.transactionDefinition = transactionDefinition;
    }

    @Override
    public CustomResult<RetBookerBorrowReturnCreateFlow> borrowReturnCreateFlow(String operater, RopBookerBorrowReturnCreateFlow rop) {

        CustomResult<RetBookerBorrowReturnCreateFlow> result=new CustomResult<>();

        LumosSelective selective_MerchDevice=new LumosSelective();
        selective_MerchDevice.addWhere("DeviceId",rop.getDeviceId());
        selective_MerchDevice.addWhere("BindStatus","1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);

        if (d_MerchDevice==null)
            return result.fail("设备未绑定商户");

        if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
            return result.fail("设备未绑定店铺");

        if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
            return result.fail("设备未绑定门店");

        BookBorrowReturnFlow d_BookBorrowReturnFlow = new BookBorrowReturnFlow();

        d_BookBorrowReturnFlow.setId(IdWork.generateGUID());
        d_BookBorrowReturnFlow.setMerchId(d_MerchDevice.getMerchId());
        d_BookBorrowReturnFlow.setStoreId(d_MerchDevice.getStoreId());
        d_BookBorrowReturnFlow.setShopId(d_MerchDevice.getShopId());
        d_BookBorrowReturnFlow.setDeviceId(rop.getDeviceId());
        d_BookBorrowReturnFlow.setCabinetId(rop.getCabinetId());
        d_BookBorrowReturnFlow.setSlotId(rop.getSlotId());
        d_BookBorrowReturnFlow.setClientUserId(rop.getClientUserId());
        d_BookBorrowReturnFlow.setIdentityType(rop.getIdentityType());
        d_BookBorrowReturnFlow.setIdentityId(rop.getIdentityId());
        d_BookBorrowReturnFlow.setStatus(1);
        d_BookBorrowReturnFlow.setCreateTime(CommonUtil.getDateTimeNow());
        d_BookBorrowReturnFlow.setCreator(IdWork.generateGUID());

        if (bookBorrowReturnFlowMapper.insert(d_BookBorrowReturnFlow) <= 0)
            return result.fail("流程创建失败");

        RetBookerBorrowReturnCreateFlow ret = new RetBookerBorrowReturnCreateFlow();
        ret.setFlowId(d_BookBorrowReturnFlow.getId());

        return result.success("流程创建成功", ret);
    }

    @Override
    public CustomResult<RetBookerBorrowReturnOpenAction>  borrowReturnOpenAction(String operater, RopBookerBorrowReturnOpenAction rop) {

        CustomResult<RetBookerBorrowReturnOpenAction> result=new CustomResult<>();

        BookBorrowReturnFlow d_BookBorrowReturnFlow = new BookBorrowReturnFlow();
        d_BookBorrowReturnFlow.setId(rop.getFlowId());
        d_BookBorrowReturnFlow.setOpenActionResult(rop.getActionResult());
        d_BookBorrowReturnFlow.setOpenActionCode(rop.getActionCode());
        d_BookBorrowReturnFlow.setOpenActionTime(CommonUtil.getDateTimeNow());
        d_BookBorrowReturnFlow.setOpenRfIds(JsonUtil.getJson(rop.getRfIds()));
        if (rop.getActionResult() == 1) {
            d_BookBorrowReturnFlow.setStatus(2);
        } else if (rop.getActionResult() == 2) {
            d_BookBorrowReturnFlow.setStatus(3);
        }
        d_BookBorrowReturnFlow.setMender(IdWork.generateGUID());
        d_BookBorrowReturnFlow.setMendTime(CommonUtil.getDateTimeNow());

        if (bookBorrowReturnFlowMapper.update(d_BookBorrowReturnFlow) <= 0)
            return result.fail("打开失败[1]");

        if (rop.getActionResult() != 1)
            return result.fail("打开失败[2]");

        RetBookerBorrowReturnOpenAction ret = new RetBookerBorrowReturnOpenAction();
        ret.setFlowId(rop.getFlowId());
        return result.success("打开成功", ret);
    }

    @Override
    public CustomResult<RetBookerBorrowReturnCloseAction>  borrowReturnCloseAction(String operater, RopBookerBorrowReturnCloseAction rop) {

        CustomResult<RetBookerBorrowReturnCloseAction> result=new CustomResult<>();

        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {

            LumosSelective selective_BookBorrowReturnFlow = new LumosSelective();
            selective_BookBorrowReturnFlow.setFields("*");
            selective_BookBorrowReturnFlow.addWhere("BookBorrowReturnFlowId", rop.getFlowId());

            //查找借还流程
            BookBorrowReturnFlow d_BookBorrowReturnFlow = bookBorrowReturnFlowMapper.findOne(selective_BookBorrowReturnFlow);
            if (d_BookBorrowReturnFlow == null) {
                lock.unlock();
                return result.fail("关闭失败[1]");
            }

            d_BookBorrowReturnFlow.setCloseActionResult(rop.getActionResult());
            d_BookBorrowReturnFlow.setCloseActionCode(rop.getActionCode());
            d_BookBorrowReturnFlow.setCloseActionTime(CommonUtil.getDateTimeNow());
            d_BookBorrowReturnFlow.setCloseRfIds(JsonUtil.getJson(rop.getRfIds()));

            if (rop.getActionResult() == 1) {
                d_BookBorrowReturnFlow.setStatus(5);
            } else if (rop.getActionResult() == 2) {
                d_BookBorrowReturnFlow.setStatus(6);
            }

            d_BookBorrowReturnFlow.setMender(IdWork.generateGUID());
            d_BookBorrowReturnFlow.setMendTime(CommonUtil.getDateTimeNow());

            if (bookBorrowReturnFlowMapper.update(d_BookBorrowReturnFlow) <= 0) {
                lock.unlock();
                return result.fail("关闭失败[2]");
            }

            if (rop.getActionResult() != 1) {
                lock.unlock();
                return result.fail("关闭失败[3]");
            }


            List<String> open_RfIds = JsonUtil.toObject(d_BookBorrowReturnFlow.getOpenRfIds(),new TypeReference<List<String>>() {});

            List<String> close_RfIds =JsonUtil.toObject(d_BookBorrowReturnFlow.getCloseRfIds(),new TypeReference<List<String>>() {});

            List<String> borrow_RfIds = new ArrayList<>();

            if(open_RfIds!=null) {
                for (String open_rfId : open_RfIds) {
                    if (close_RfIds != null) {
                        if (!close_RfIds.contains(open_rfId)) {
                            borrow_RfIds.add(open_rfId);
                        }
                    }
                }
            }

            List<String> return_RfIds = new ArrayList<>();

            if(close_RfIds!=null) {
                for (String close_rfId : close_RfIds) {
                    if(open_RfIds!=null) {
                        if (!open_RfIds.contains(close_rfId)) {
                            return_RfIds.add(close_rfId);
                        }
                    }
                }
            }

            //借阅的书籍
            for (String borrow_RfId : borrow_RfIds) {

                LumosSelective selective_BookBorrowReturnFlowBook = new LumosSelective();
                selective_BookBorrowReturnFlowBook.setFields("*");
                selective_BookBorrowReturnFlowBook.addWhere("FlowId", rop.getFlowId());
                selective_BookBorrowReturnFlowBook.addWhere("SkuRfId", borrow_RfId);

                BookBorrowReturnFlowData d_BookBorrowReturnFlowData = bookBorrowReturnFlowDataMapper.findOne(selective_BookBorrowReturnFlowBook);
                if(d_BookBorrowReturnFlowData ==null) {
                    d_BookBorrowReturnFlowData = new BookBorrowReturnFlowData();
                    d_BookBorrowReturnFlowData.setId(IdWork.generateGUID());
                    d_BookBorrowReturnFlowData.setFlowId(d_BookBorrowReturnFlow.getId());
                    d_BookBorrowReturnFlowData.setClientUserId(d_BookBorrowReturnFlow.getClientUserId());
                    d_BookBorrowReturnFlowData.setSkuRfId(borrow_RfId);
                    d_BookBorrowReturnFlowData.setBorrowTime(CommonUtil.getDateTimeNow());
                    d_BookBorrowReturnFlowData.setCreator(IdWork.generateGUID());
                    d_BookBorrowReturnFlowData.setCreateTime(CommonUtil.getDateTimeNow());

                    if (bookBorrowReturnFlowDataMapper.insert(d_BookBorrowReturnFlowData) <= 0) {
                        lock.unlock();
                        return result.fail("关闭失败[4]");
                    }
                }
            }


            RetBookerBorrowReturnCloseAction ret = new RetBookerBorrowReturnCloseAction();
            ret.setFlowId(rop.getFlowId());

            List<BookBean> borrowBooks = new ArrayList<>();

            borrowBooks.add(new BookBean("1", "1", "安徒生童话故事", "1", "1"));
            borrowBooks.add(new BookBean("1", "1", "这个杀手不太冷静", "1", "1"));


            List<BookBean> returnBooks = new ArrayList<>();
            returnBooks.add(new BookBean("1", "1", "西游记", "1", "1"));
            returnBooks.add(new BookBean("1", "1", "红楼梦", "1", "1"));

            ret.setBorrowBooks(borrowBooks);
            ret.setReturnBooks(returnBooks);

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  result.success("保存成功",ret);
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return result.fail("保存失败,服务器异常");
        }
    }

}