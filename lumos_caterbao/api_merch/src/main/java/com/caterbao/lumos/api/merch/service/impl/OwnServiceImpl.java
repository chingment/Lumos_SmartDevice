package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RetOwnGetInfo;
import com.caterbao.lumos.api.merch.rop.model.MenuModel;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.merch.service.OwnService;
import com.caterbao.lumos.locals.common.PasswordUtil;
import com.caterbao.lumos.locals.dal.IdWork;
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

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMerchUserMapper sysMerchUserMapper;

    @Autowired
    private  RedisTemplate redisTemplate;

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

        SysMerchUser d_SysMerchUser = sysMerchUserMapper.findByUserId(d_User.getId());

        if (d_SysMerchUser == null)
            return CustomResult.fail("该账号未授权");

        String token = IdWork.generateGUID();

        Map<String, Object> ret = new HashMap<>();
        ret.put("token", token);

        Map<String, Object> token_val = new HashMap<>();
        token_val.put("id", d_User.getId());
        token_val.put("userName", d_User.getUserName());
        token_val.put("merchId", d_SysMerchUser.getMerchId());

        redisTemplate.opsForValue().set("token:" + token, token_val, 1, TimeUnit.HOURS);

        return CustomResult.success("登录成功", ret);

    }


    @Override
    public CustomResult getInfo(String operater,String userId) {

        RetOwnGetInfo ret = new RetOwnGetInfo();

        List<SysMenu> d_Menus = sysUserMapper.getMenusByUserId(userId);

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
