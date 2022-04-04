package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopAdminUserAdd;
import com.caterbao.lumos.api.merch.rop.RopAdminUserEdit;
import com.caterbao.lumos.api.merch.rop.RopAdminUserList;
import com.caterbao.lumos.api.merch.service.AdminUserService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.SysMerchUserMapper;
import com.caterbao.lumos.locals.dal.mapper.SysUserMapper;
import com.caterbao.lumos.locals.dal.pojo.SysMerchUser;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import com.caterbao.lumos.locals.dal.vw.SysMerchUserVw;
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
public class AdminUserServiceImpl implements AdminUserService {

    private SysUserMapper sysUserMapper;
    private SysMerchUserMapper sysMerchUserMapper;
    private PlatformTransactionManager platformTransactionManager;
    private TransactionDefinition transactionDefinition;

    @Autowired(required = false)
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Autowired(required = false)
    public void setSysMerchUserMapper(SysMerchUserMapper sysMerchUserMapper) {
        this.sysMerchUserMapper = sysMerchUserMapper;
    }

    @Autowired(required = false)
    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Autowired(required = false)
    public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
        this.transactionDefinition = transactionDefinition;
    }

    private Lock lock = new ReentrantLock();

    @Override
    public CustomResult<Object> list(String operater, String merchId, RopAdminUserList rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize,"CreateTime Desc");

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("UserName",rop.getUserName());
        selective.addWhere("IsMaster","0");
        List<SysMerchUserVw> d_MerchUsers = sysMerchUserMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (SysMerchUserVw d_MerchUser:
                d_MerchUsers ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_MerchUser.getId());
            item.put("fullName",d_MerchUser.getFullName());
            item.put("userName",d_MerchUser.getUserName());
            item.put("phoneNumber",d_MerchUser.getPhoneNumber());
            item.put("isDisable",d_MerchUser.isDisable());
            item.put("email",d_MerchUser.getEmail());
            item.put("createTime", CommonUtil.toDateTimeStr(d_MerchUser.getCreateTime()));
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
    public CustomResult<Object>  add(String operater, String merchId, RopAdminUserAdd rop) {

        CustomResult<Object> result = new CustomResult<>();

        if(CommonUtil.isEmpty(rop.getUserName()))
            return result.fail("用户名不能为空");

        if(CommonUtil.isEmpty(rop.getUserName()))
            return result.fail("密码不能为空");

        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {

            if(sysUserMapper.isExistUserName(rop.getUserName())>0) {
                lock.unlock();
                return result.fail("用户名已经存在");
            }

            SysUser d_SysUser=new SysUser();
            d_SysUser.setId(IdWork.buildGuId());
            d_SysUser.setUserName(rop.getUserName());
            d_SysUser.setSecurityStamp(IdWork.buildGuId());
            d_SysUser.setPasswordHash(PasswordUtil.encryBySHA256(rop.getPassword(),d_SysUser.getSecurityStamp()));
            d_SysUser.setFullName(rop.getFullName());
            d_SysUser.setPhoneNumber(rop.getPhoneNumber());
            d_SysUser.setEmail(rop.getEmail());
            d_SysUser.setAvatar(JsonUtil.getJson(rop.getAvatar()));
            d_SysUser.setIsDisable(false);
            d_SysUser.setCreator(operater);
            d_SysUser.setCreateTime(CommonUtil.getDateTimeNow());
            long r_SysUser_Insert = sysUserMapper.insert(d_SysUser);

            if (r_SysUser_Insert <= 0) {
                lock.unlock();
                return result.fail("保存失败");
            }

            SysMerchUser d_SysMerchUser=new SysMerchUser();
            d_SysMerchUser.setId(IdWork.buildGuId());
            d_SysMerchUser.setMaster(false);
            d_SysMerchUser.setMerchId(merchId);
            d_SysMerchUser.setUserId(d_SysUser.getId());
            long r_MerchUser_Insert = sysMerchUserMapper.insert(d_SysMerchUser);

            if (r_MerchUser_Insert <= 0) {
                lock.unlock();
                return result.fail("保存失败");
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  result.success("保存成功");
        }
        catch (Exception ex) {
            platformTransactionManager.rollback(transaction);
            ex.printStackTrace();
            lock.unlock();
            return result.fail("保存失败,服务器异常");
        }
    }

    @Override
    public CustomResult<Object> init_edit(String operater, String merchId, String userId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective_SysUser = new LumosSelective();
        selective_SysUser.setFields("Id,UserName,FullName,PhoneNumber,Email,Avatar,IsDisable");
        selective_SysUser.addWhere("UserId", userId);

        SysUser d_SysUser = sysUserMapper.findOne(selective_SysUser);

        HashMap<String,Object> ret=new HashMap<>();

        ret.put("id",d_SysUser.getId());
        ret.put("userName",d_SysUser.getUserName());
        ret.put("fullName",d_SysUser.getFullName());
        ret.put("phoneNumber",d_SysUser.getPhoneNumber());
        ret.put("email",d_SysUser.getEmail());
        ret.put("avatar",JsonUtil.toObject(d_SysUser.getAvatar()));
        ret.put("isDisable",d_SysUser.getIsDisable());

        return result.success("",ret);
    }

    @Override
    public CustomResult<Object> edit(String operater, String merchId, RopAdminUserEdit rop) {

        CustomResult<Object> result = new CustomResult<>();

        if(CommonUtil.isEmpty(rop.getId()))
            return result.fail("用户Id不能为空");

        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {

            LumosSelective selective_SysUser=new LumosSelective();
            selective_SysUser.setFields("Id,UserName,PasswordHash,SecurityStamp,IsDisable");
            selective_SysUser.addWhere("UserId",rop.getId());

            SysUser d_SysUser=sysUserMapper.findOne(selective_SysUser);

            if(!CommonUtil.isEmpty(rop.getPassword())) {
                d_SysUser.setPasswordHash(PasswordUtil.encryBySHA256(rop.getPassword(), d_SysUser.getSecurityStamp()));
            }

            d_SysUser.setFullName(rop.getFullName());
            d_SysUser.setPhoneNumber(rop.getPhoneNumber());
            d_SysUser.setEmail(rop.getEmail());
            d_SysUser.setAvatar(JsonUtil.getJson(rop.getAvatar()));
            d_SysUser.setIsDisable(rop.getIsDisable());
            d_SysUser.setMender(operater);
            d_SysUser.setMendTime(CommonUtil.getDateTimeNow());
            long r_SysUser_Update = sysUserMapper.update(d_SysUser);

            if (r_SysUser_Update <= 0) {
                lock.unlock();
                return result.fail("保存失败");
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  result.success("保存成功");
        }
        catch (Exception ex) {
            platformTransactionManager.rollback(transaction);
            ex.printStackTrace();
            lock.unlock();
            return result.fail("保存失败,服务器异常");
        }
    }
}
