package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.service.OwnService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.PasswordUtil;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.MerchDeviceMapper;
import com.caterbao.lumos.locals.dal.mapper.SysMerchUserMapper;
import com.caterbao.lumos.locals.dal.mapper.SysUserMapper;
import com.caterbao.lumos.locals.dal.pojo.SysMerchUser;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import com.caterbao.lumos.locals.dal.vw.MerchUserVw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnServiceImpl implements OwnService {

    private SysUserMapper sysUserMapper;
    private SysMerchUserMapper sysMerchUserMapper;
    private MerchDeviceMapper merchDeviceMapper;

    @Autowired(required = false)
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Autowired(required = false)
    public void setSysMerchUserMapper(SysMerchUserMapper sysMerchUserMapper) {
        this.sysMerchUserMapper = sysMerchUserMapper;
    }

    @Autowired(required = false)
    public void setMerchDeviceMapper(MerchDeviceMapper merchDeviceMapper) {
        this.merchDeviceMapper = merchDeviceMapper;
    }

    @Override
    public CustomResult<RetOwnLogin> loginByAccount(RopOwnLoginByAccount rop) {

        CustomResult<RetOwnLogin> result = new CustomResult<>();

        LumosSelective selective_SysUser=new LumosSelective();
        selective_SysUser.setFields("Id,UserName,FullName,Avatar, PasswordHash,SecurityStamp,IsDisable");
        selective_SysUser.addWhere("UserName",rop.getUserName());

        SysUser d_SysUser = sysUserMapper.findOne(selective_SysUser);

        if (d_SysUser == null)
            return result.fail("账号或密码错误");

        String passwordHash = d_SysUser.getPasswordHash();

        boolean isFlag = PasswordUtil.veriflyBySHA256(rop.getPassword(), d_SysUser.getSecurityStamp(), passwordHash);

        if (!isFlag)
            return result.fail("账号或密码错误");

        if(d_SysUser.getIsDisable())
            return result.fail("该账号已被停用");

        LumosSelective selective_SysMerchUser=new LumosSelective();
        selective_SysMerchUser.setFields("UserId,MerchId");
        selective_SysMerchUser.addWhere("UserId",d_SysUser.getId());

        MerchUserVw d_SysMerchUser = sysMerchUserMapper.findOne(selective_SysMerchUser);

        if (d_SysMerchUser == null)
            return result.fail("该账号未授权");

        LumosSelective selective_MerchDevice=new LumosSelective();
        selective_MerchDevice.addWhere("MerchId",d_SysMerchUser.getMerchId());
        selective_MerchDevice.addWhere("DeviceId",rop.getDeviceId());
        selective_MerchDevice.addWhere("BindStatus","1");

        long validCount= merchDeviceMapper.count(selective_MerchDevice);

        if(validCount<=0)
            return result.fail("该账号未授权");

        RetOwnLogin ret=new RetOwnLogin();
        ret.setUserId(d_SysUser.getId());
        ret.setUserName(d_SysUser.getUserName());
        ret.setFullName(d_SysUser.getFullName());
        ret.setAvatar(d_SysUser.getAvatar());
        return result.success("登录成功", ret);
    }

    @Override
    public CustomResult<RetOwnLogout> logout(String operater, RopOwnLogout rop) {
        CustomResult<RetOwnLogout> reslut=new CustomResult<>();
        RetOwnLogout ret = new RetOwnLogout();
        return reslut.success("退出成功",ret);
    }

    @Override
    public CustomResult<RetOwnGetInfo> getInfo(String operater, RopOwnGetInfo rop) {

        CustomResult<RetOwnGetInfo> result=new CustomResult<>();

        LumosSelective selective_SysUser=new LumosSelective();
        selective_SysUser.setFields("Id,UserName,FullName,Avatar");
        selective_SysUser.addWhere("UserId",rop.getUserId());

        SysUser d_SysUser = sysUserMapper.findOne(selective_SysUser);

        if (d_SysUser == null)
            return result.fail("信息不存在");

        RetOwnGetInfo ret = new RetOwnGetInfo();

        ret.setUserId(d_SysUser.getId());
        ret.setUserName(d_SysUser.getUserName());
        ret.setFullName(d_SysUser.getFullName());
        ret.setAvatar(d_SysUser.getAvatar());

        return result.success("获取成功",ret);
    }

    @Override
    public CustomResult<RetOwnSaveInfo> saveInfo(String operater, RopOwnSaveInfo rop) {

        CustomResult<RetOwnSaveInfo> result=new CustomResult<>();


        LumosSelective selective_SysUser = new LumosSelective();
        selective_SysUser.setFields("Id,UserName,PasswordHash,SecurityStamp,Avatar");
        selective_SysUser.addWhere("UserId", rop.getUserId());

        SysUser d_SysUser = sysUserMapper.findOne(selective_SysUser);

        if (d_SysUser == null)
            return result.fail("信息不存在");

        d_SysUser.setFullName(rop.getFullName());
        d_SysUser.setMender(d_SysUser.getId());
        d_SysUser.setMendTime(CommonUtil.getDateTimeNow());
        if (CommonUtil.isEmpty(rop.getPassword())) {
            d_SysUser.setPasswordHash(null);
        } else {
            d_SysUser.setPasswordHash(PasswordUtil.encryBySHA256(rop.getPassword(), d_SysUser.getSecurityStamp()));
        }

        if (sysUserMapper.update(d_SysUser) <= 0)
            return result.fail("保存失败");

        RetOwnSaveInfo ret = new RetOwnSaveInfo();

        ret.setUserId(d_SysUser.getId());
        ret.setUserName(d_SysUser.getUserName());
        ret.setFullName(d_SysUser.getFullName());
        ret.setAvatar(d_SysUser.getAvatar());

        return result.success("保存成功", ret);
    }
}
