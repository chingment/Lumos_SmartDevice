package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopAdminUserAdd;
import com.caterbao.lumos.api.merch.rop.RopAdminUserEdit;
import com.caterbao.lumos.api.merch.rop.RopAdminUserList;
import com.caterbao.lumos.api.merch.service.AdminUserService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.MerchDeviceMapper;
import com.caterbao.lumos.locals.dal.mapper.SysMerchUserMapper;
import com.caterbao.lumos.locals.dal.mapper.SysUserMapper;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import com.caterbao.lumos.locals.dal.pojo.SysMerchUser;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import com.caterbao.lumos.locals.dal.vw.MerchUserVw;
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

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMerchUserMapper sysMerchUserMapper;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    private Lock lock = new ReentrantLock();

    @Override
    public CustomResult init_add(String operater, String merchId) {
        return CustomResult.success("");
    }

    @Override
    public CustomResult add(String operater, String merchId, RopAdminUserAdd rop) {

        if(CommonUtil.isEmpty(rop.getUserName()))
            return CustomResult.fail("用户名不能为空");

        if(CommonUtil.isEmpty(rop.getUserName()))
            return CustomResult.fail("密码不能为空");

        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {

            if(sysUserMapper.isExistUserName(rop.getUserName())>0) {
                lock.unlock();
                return CustomResult.fail("用户名已经存在");
            }

            SysUser d_SysUser=new SysUser();
            d_SysUser.setId(IdWork.generateGUID());
            d_SysUser.setUserName(rop.getUserName());
            d_SysUser.setSecurityStamp(IdWork.generateGUID());
            d_SysUser.setPasswordHash(PasswordUtil.encryBySHA256(rop.getPassword(),d_SysUser.getSecurityStamp()));
            d_SysUser.setFullName(rop.getFullName());
            d_SysUser.setPhoneNumber(rop.getPhoneNumber());
            d_SysUser.setEmail(rop.getEmail());
            d_SysUser.setAvatar(rop.getAvatar());
            d_SysUser.setIsDisable(false);
            d_SysUser.setCreator(operater);
            d_SysUser.setCreateTime(CommonUtil.getDateTimeNow());
            long r_SysUser_Insert = sysUserMapper.insert(d_SysUser);

            if (r_SysUser_Insert <= 0) {
                lock.unlock();
                return CustomResult.fail("保存失败");
            }

            SysMerchUser d_SysMerchUser=new SysMerchUser();
            d_SysMerchUser.setId(IdWork.generateGUID());
            d_SysMerchUser.setMaster(false);
            d_SysMerchUser.setMerchId(merchId);
            d_SysMerchUser.setUserId(d_SysUser.getId());
            long r_MerchUser_Insert = sysMerchUserMapper.insert(d_SysMerchUser);

            if (r_MerchUser_Insert <= 0) {
                lock.unlock();
                return CustomResult.fail("保存失败");
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  CustomResult.success("保存成功");
        }
        catch (Exception ex) {
            platformTransactionManager.rollback(transaction);
            ex.printStackTrace();
            lock.unlock();
            return CustomResult.fail("保存失败,服务器异常");
        }
    }

    @Override
    public CustomResult list(String operater, String merchId, RopAdminUserList rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("UserName",rop.getUserName());
        List<MerchUserVw> d_MerchUsers = sysMerchUserMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (MerchUserVw d_MerchUser:
                d_MerchUsers ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_MerchUser.getId());
            item.put("fullName",d_MerchUser.getFullName());
            item.put("userName",d_MerchUser.getUserName());
            item.put("phoneNumber",d_MerchUser.getPhoneNumber());
            item.put("isDisable",d_MerchUser.isDisable());
            item.put("email",d_MerchUser.getEmail());
            item.put("createTime", CommonUtil.toDateTime(d_MerchUser.getCreateTime()));
            items.add(item);
        }

        long total = page.getTotal();
        PageResult<Object> ret = new PageResult<>();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return CustomResult.success("",ret);
    }

    @Override
    public CustomResult init_edit(String operater, String merchId, String userId) {

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
        ret.put("avatar",d_SysUser.getAvatar());
        ret.put("isDisable",d_SysUser.getIsDisable());

        return CustomResult.success("",ret);
    }

    @Override
    public CustomResult edit(String operater, String merchId, RopAdminUserEdit rop) {

        if(CommonUtil.isEmpty(rop.getId()))
            return CustomResult.fail("用户Id不能为空");

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
            d_SysUser.setAvatar(rop.getAvatar());
            d_SysUser.setIsDisable(rop.getIsDisable());
            d_SysUser.setMender(operater);
            d_SysUser.setMendTime(CommonUtil.getDateTimeNow());
            long r_SysUser_Update = sysUserMapper.update(d_SysUser);

            if (r_SysUser_Update <= 0) {
                lock.unlock();
                return CustomResult.fail("保存失败");
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  CustomResult.success("保存成功");
        }
        catch (Exception ex) {
            platformTransactionManager.rollback(transaction);
            ex.printStackTrace();
            lock.unlock();
            return CustomResult.fail("保存失败,服务器异常");
        }
    }
}
