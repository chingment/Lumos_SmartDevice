package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.BorrowerService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.IcCardMapper;
import com.caterbao.lumos.locals.dal.mapper.SysClientUserMapper;
import com.caterbao.lumos.locals.dal.mapper.SysUserMapper;
import com.caterbao.lumos.locals.dal.pojo.IcCard;
import com.caterbao.lumos.locals.dal.pojo.SysClientUser;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import com.caterbao.lumos.locals.dal.vw.SysClientUserVw;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
public class BorrowerServiceImpl implements BorrowerService{

    private SysUserMapper sysUserMapper;
    private SysClientUserMapper sysClientUserMapper;
    private IcCardMapper icCardMapper;
    private PlatformTransactionManager platformTransactionManager;
    private TransactionDefinition transactionDefinition;

    @Autowired
    public void setSysClientUserMapper(SysClientUserMapper sysClientUserMapper) {
        this.sysClientUserMapper = sysClientUserMapper;
    }

    @Autowired(required = false)
    public void setIcCardMapper(IcCardMapper icCardMapper) {
        this.icCardMapper = icCardMapper;
    }

    @Autowired(required = false)
    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Autowired(required = false)
    public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
        this.transactionDefinition = transactionDefinition;
    }

    @Autowired(required = false)
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    private Lock lock = new ReentrantLock();

    @Override
    public CustomResult<Object> list(String operater, String merchId, RopBorrowerList rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize,"CreateTime Desc");

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);

        List<SysClientUserVw> d_ClientUsers = sysClientUserMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (SysClientUserVw d_ClientUser:
                d_ClientUsers ) {

            HashMap<String, Object> item = new HashMap<>();


            selective=new LumosSelective();
            selective.setFields("*");
            selective.addWhere("MerchId",merchId);
            selective.addWhere("ClientUserId",d_ClientUser.getId());

            List<Object> m_IcCards=new ArrayList<>();

            List<IcCard> d_IcCards = icCardMapper.find(selective);

            for (IcCard d_IcCard: d_IcCards ) {
                HashMap<String, Object> m_IcCard = new HashMap<>();
                m_IcCard.put("cardNo", d_IcCard.getCardNo());
                m_IcCard.put("isDisable", d_IcCard.getDisable());
                m_IcCard.put("fullName", d_IcCard.getFullName());
                m_IcCards.add(m_IcCard);
            }

            item.put("id", d_ClientUser.getId());
            item.put("icCards",m_IcCards);
            item.put("fullName", d_ClientUser.getFullName());
            item.put("isDisable",d_ClientUser.isDisable());
            item.put("createTime", CommonUtil.toDateTimeStr(d_ClientUser.getCreateTime()));
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
    public CustomResult<Object>  init_add(String operater, String merchId) {
        CustomResult<Object> result = new CustomResult<>();
        return result.success("");
    }

    @Override
    public CustomResult<Object>  add(String operater, String merchId, RopBorrowerAdd rop) {

        CustomResult<Object> result = new CustomResult<>();

        if(CommonUtil.isEmpty(rop.getCardNo()))
            return result.fail("??????????????????");

        if(CommonUtil.isEmpty(rop.getCardPwd()))
            return result.fail("??????????????????");

        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {

            if (icCardMapper.isExistCardNo(null, merchId, rop.getCardNo()) > 0) {
                lock.unlock();
                return result.fail("??????????????????");
            }

            SysUser d_SysUser = new SysUser();
            d_SysUser.setId(IdWork.buildGuId());
            d_SysUser.setUserName(IdWork.buildGuId());
            d_SysUser.setSecurityStamp(IdWork.buildGuId());
            d_SysUser.setPasswordHash(PasswordUtil.encryBySHA256(rop.getCardPwd(), d_SysUser.getSecurityStamp()));
            d_SysUser.setFullName(rop.getFullName());
            d_SysUser.setPhoneNumber(rop.getPhoneNumber());
            d_SysUser.setEmail(rop.getEmail());
            d_SysUser.setAvatar(JsonUtil.getJson(rop.getAvatar()));
            d_SysUser.setIsDisable(false);
            d_SysUser.setCreator(operater);
            d_SysUser.setCreateTime(CommonUtil.getDateTimeNow());

            if (sysUserMapper.insert(d_SysUser) <= 0) {
                lock.unlock();
                return result.fail("????????????");
            }

            SysClientUser d_SysClientUser = new SysClientUser();
            d_SysClientUser.setId(IdWork.buildGuId());
            d_SysClientUser.setMerchId(merchId);
            d_SysClientUser.setUserId(d_SysUser.getId());

            if (sysClientUserMapper.insert(d_SysClientUser) <= 0) {
                lock.unlock();
                return result.fail("????????????");
            }

            IcCard d_IcCard = new IcCard();
            d_IcCard.setId(IdWork.buildGuId());
            d_IcCard.setMerchId(merchId);
            d_IcCard.setClientUserId(d_SysClientUser.getUserId());
            d_IcCard.setCardNo(rop.getCardNo());
            d_IcCard.setCardType(1);
            d_IcCard.setSecurityStamp(IdWork.buildGuId());
            d_IcCard.setCardPwdHash(PasswordUtil.encryBySHA256(rop.getCardPwd(), d_IcCard.getSecurityStamp()));
            d_IcCard.setDisable(false);
            d_IcCard.setCreator(operater);
            d_IcCard.setCreateTime(CommonUtil.getDateTimeNow());

            if (icCardMapper.insert(d_IcCard) <= 0) {
                lock.unlock();
                return result.fail("????????????");
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return result.success("????????????");
        }
        catch (Exception ex) {
            platformTransactionManager.rollback(transaction);
            ex.printStackTrace();
            lock.unlock();
            return result.fail("????????????,???????????????");
        }
    }

    @Override
    public CustomResult<Object> init_edit(String operater, String merchId, String borrowerId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("UserId", borrowerId);
        SysClientUserVw d_Sys_ClientUserVw = sysClientUserMapper.findOne(selective);

        if (d_Sys_ClientUserVw == null)
            return result.fail("??????????????????");


        HashMap<String,Object> ret=new HashMap<>();

        ret.put("id", d_Sys_ClientUserVw.getId());
        ret.put("fullName", d_Sys_ClientUserVw.getFullName());
        ret.put("avatar",JsonUtil.toObject(d_Sys_ClientUserVw.getAvatar()));
        ret.put("email", d_Sys_ClientUserVw.getEmail());
        ret.put("phoneNumber", d_Sys_ClientUserVw.getPhoneNumber());

        return result.success("????????????",ret);


    }

    @Override
    public CustomResult<Object> edit(String operater, String merchId, RopBorrowerEdit rop) {

        CustomResult<Object> result = new CustomResult<>();
        return result.success("");
    }
}
