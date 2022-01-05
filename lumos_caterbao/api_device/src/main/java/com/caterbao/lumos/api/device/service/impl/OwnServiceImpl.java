package com.caterbao.lumos.api.device.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.service.OwnService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.PasswordUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.mapper.SysUserMapper;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OwnServiceImpl implements OwnService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    public OwnServiceImpl() {
    }

    @Override
    public CustomResult loginByAccount(RopOwnLoginByAccount rop) {

        CustomResult result = new CustomResult();

        SysUser d_User = sysUserMapper.findByUserName(rop.getUserName());

        if (d_User == null)
            return CustomResult.fail("账号或密码错误");

        String passwordHash = d_User.getPasswordHash();

        boolean isFlag = PasswordUtil.veriflyBySHA256(rop.getPassword(), d_User.getSecurityStamp(), passwordHash);

        if (!isFlag)
            return CustomResult.fail("账号或密码错误");

        RetOwnLogin ret=new RetOwnLogin();
        ret.setUserId(d_User.getId());
        ret.setUserName(d_User.getUserName());
        ret.setFullName(d_User.getFullName());
        ret.setAvatar(d_User.getAvatar());
        return CustomResult.success("登录成功", ret);
    }

    @Override
    public CustomResult logout(String operater, RopOwnLogout rop) {
        RetOwnLogout ret = new RetOwnLogout();
        return CustomResult.success("退出成功",ret);
    }

    @Override
    public CustomResult getInfo(String operater, RopOwnGetInfo rop) {

        RetOwnGetInfo ret = new RetOwnGetInfo();

        SysUser d_User = sysUserMapper.findByUserId(rop.getUserId());

        if (d_User == null)
            return CustomResult.fail("信息不存在");

        ret.setUserId(d_User.getId());
        ret.setUserName(d_User.getUserName());
        ret.setFullName(d_User.getFullName());
        ret.setAvatar(d_User.getAvatar());

        return CustomResult.success("获取成功",ret);
    }

    @Override
    public CustomResult saveInfo(String operater, RopOwnSaveInfo rop) {

        SysUser d_User = sysUserMapper.findByUserId(rop.getUserId());

        if (d_User == null)
            return CustomResult.fail("信息不存在");

        d_User.setFullName(rop.getFullName());
        d_User.setMender(d_User.getId());
        d_User.setMendTime(CommonUtil.getDateTimeNow());
        if(StringUtils.isEmpty(rop.getPassword())) {
            d_User.setPasswordHash(null);
        }
        else
        {
            d_User.setPasswordHash(PasswordUtil.encryBySHA256(rop.getPassword(),d_User.getSecurityStamp()));
        }

        sysUserMapper.update(d_User);

        RetOwnSaveInfo ret = new RetOwnSaveInfo();

        ret.setUserId(d_User.getId());
        ret.setUserName(d_User.getUserName());
        ret.setFullName(d_User.getFullName());
        ret.setAvatar(d_User.getAvatar());

        return CustomResult.success("保存成功", ret);
    }
}
