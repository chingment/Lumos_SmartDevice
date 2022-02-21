package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RetOwnGetInfo;
import com.caterbao.lumos.api.merch.rop.model.MenuModel;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.merch.service.OwnService;
import com.caterbao.lumos.locals.common.PasswordUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.SysMerchUserMapper;
import com.caterbao.lumos.locals.dal.mapper.SysUserMapper;
import com.caterbao.lumos.locals.dal.pojo.SysMenu;
import com.caterbao.lumos.locals.dal.pojo.SysMerchUser;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service

public class OwnServiceImpl implements OwnService {

    private SysUserMapper sysUserMapper;
    private SysMerchUserMapper sysMerchUserMapper;
    private  RedisTemplate redisTemplate;

    @Autowired(required = false)
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Autowired(required = false)
    public void setSysMerchUserMapper(SysMerchUserMapper sysMerchUserMapper) {
        this.sysMerchUserMapper = sysMerchUserMapper;
    }

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CustomResult loginByAccount(RopOwnLoginByAccount rop) {

        LumosSelective selective_SysUser=new LumosSelective();
        selective_SysUser.setFields("Id,UserName,PasswordHash,SecurityStamp,IsDisable");
        selective_SysUser.addWhere("UserName",rop.getUserName());

        SysUser d_SysUser = sysUserMapper.findOne(selective_SysUser);

        if (d_SysUser == null)
            return CustomResult.fail("账号或密码错误");

        String passwordHash = d_SysUser.getPasswordHash();

        boolean isFlag = PasswordUtil.veriflyBySHA256(rop.getPassword(), d_SysUser.getSecurityStamp(), passwordHash);

        if (!isFlag)
            return CustomResult.fail("账号或密码错误");

        LumosSelective selective_SysMerchUser=new LumosSelective();
        selective_SysMerchUser.setFields("UserId,MerchId");
        selective_SysMerchUser.addWhere("UserId",d_SysUser.getId());

        SysMerchUser d_SysMerchUser = sysMerchUserMapper.findOne(selective_SysMerchUser);

        if (d_SysMerchUser == null)
            return CustomResult.fail("该账号未授权");

        if(d_SysUser.getIsDisable())
            return CustomResult.fail("该账号已被停用");

        String token = IdWork.generateGUID();

        Map<String, Object> ret = new HashMap<>();
        ret.put("token", token);

        Map<String, Object> token_val = new HashMap<>();
        token_val.put("id", d_SysUser.getId());
        token_val.put("userName", d_SysUser.getUserName());
        token_val.put("merchId", d_SysMerchUser.getMerchId());

        redisTemplate.opsForValue().set("token:" + token, token_val, 1, TimeUnit.HOURS);

        return CustomResult.success("登录成功", ret);

    }


    @Override
    public CustomResult getInfo(String operater,String userId) {

        RetOwnGetInfo ret = new RetOwnGetInfo();

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("UserId",userId);


        List<SysMenu> d_Menus = sysUserMapper.getMenusByUserId(selective);

        ret.setUserName("chingment");

        List<MenuModel> r_Menus=new ArrayList<>();
        for (SysMenu d_Menu : d_Menus) {
            MenuModel r_Menu = new MenuModel();
            r_Menu.setId(d_Menu.getId());
            r_Menu.setCode(d_Menu.getCode());
            r_Menu.setTitle(d_Menu.getTitle());
            r_Menu.setpId(d_Menu.getpId());
            r_Menu.setComponent(d_Menu.getComponent());
            r_Menu.setPath(d_Menu.getPath());
            r_Menu.setIcon(d_Menu.getIcon());
            r_Menu.setIsNavBar(d_Menu.getNavBar());
            r_Menu.setIsSideBar(d_Menu.getSideBar());
            r_Menu.setIsRouter(d_Menu.getRouter());
            r_Menus.add(r_Menu);
        }

        ret.setMenus(r_Menus);

        return CustomResult.success("获取成功", ret);
    }
}
