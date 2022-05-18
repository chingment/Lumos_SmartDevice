package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopOwnChangePassword;
import com.caterbao.lumos.api.merch.rop.RopOwnLogout;
import com.caterbao.lumos.api.merch.rop.vo.MenuVo;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.merch.service.OwnService;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.common.PasswordUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.SysMerchUserMapper;
import com.caterbao.lumos.locals.dal.mapper.SysUserMapper;
import com.caterbao.lumos.locals.dal.pojo.SysMenu;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import com.caterbao.lumos.locals.dal.vw.SysMerchUserVw;
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
    public CustomResult<Object> loginByAccount(RopOwnLoginByAccount rop) {

        CustomResult<Object> result = new CustomResult<>();


        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,UserName,PasswordHash,SecurityStamp,IsDisable");
        selective.addWhere("UserName",rop.getUserName());

        SysUser d_SysUser = sysUserMapper.findOne(selective);

        if (d_SysUser == null)
            return result.fail("账号或密码错误");

        String passwordHash = d_SysUser.getPasswordHash();

        boolean isFlag = PasswordUtil.veriflyBySHA256(rop.getPassword(), d_SysUser.getSecurityStamp(), passwordHash);

        if (!isFlag)
            return result.fail("账号或密码错误");

        selective=new LumosSelective();
        selective.setFields("UserId,MerchId");
        selective.addWhere("UserId",d_SysUser.getId());

        SysMerchUserVw d_SysMerchUser = sysMerchUserMapper.findOne(selective);

        if (d_SysMerchUser == null)
            return result.fail("该账号未授权");

        if(d_SysUser.getIsDisable())
            return result.fail("该账号已被停用");

        String token = IdWork.buildGuId();

        Map<String, Object> ret = new HashMap<>();
        ret.put("token", token);

        Map<String, Object> token_val = new HashMap<>();
        token_val.put("id", d_SysUser.getId());
        token_val.put("userName", d_SysUser.getUserName());
        token_val.put("merchId", d_SysMerchUser.getMerchId());

        redisTemplate.opsForValue().set("token:" + token, JsonUtil.getJson(token_val), 1, TimeUnit.HOURS);

        return result.success("登录成功", ret);

    }

    @Override
    public CustomResult<Object> getInfo(String operater,String userId,String mode) {

        CustomResult<Object> result = new CustomResult<>();


        HashMap<String,Object> ret=new HashMap<>();

      //  RetOwnGetInfo ret = new RetOwnGetInfo();

        if(mode.equals("0")) {
            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("UserId", userId);

            List<SysMenu> d_Menus = sysUserMapper.getMenusByUserId(selective);

            ret.put("userName", "");

            List<MenuVo> r_Menus = new ArrayList<>();
            for (SysMenu d_Menu : d_Menus) {
                MenuVo r_Menu = new MenuVo();
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

            ret.put("menus", r_Menus);
        }
        else {
            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("UserId", userId);

            SysMerchUserVw d_MerchUser = sysMerchUserMapper.findOne(selective);

            ret.put("userName", d_MerchUser.getUserName());
            ret.put("fullName", d_MerchUser.getFullName());
            ret.put("phoneNumber", d_MerchUser.getPhoneNumber());
            ret.put("email", d_MerchUser.getEmail());
            ret.put("roleNames", "");
        }


        return result.success("获取成功", ret);
    }

    @Override
    public CustomResult<Object> logout(String operater, RopOwnLogout rop) {
        CustomResult<Object> result = new CustomResult<>();

        if (rop != null) {
            redisTemplate.delete("token:" + rop.getToken());
        }

        return result.success("退出成功");
    }

    @Override
    public CustomResult<Object> changePassword(String operater, RopOwnChangePassword rop) {
        CustomResult<Object> result = new CustomResult<>();

        if (CommonUtil.isEmpty(rop.getNewPassword())) {
            return result.fail("新密码不能为空");
        }

        LumosSelective selective= new LumosSelective();
        selective.setFields("Id,UserName,PasswordHash,SecurityStamp,IsDisable");
        selective.addWhere("UserId", operater);

        SysUser d_SysUser = sysUserMapper.findOne(selective);

        if (d_SysUser == null)
            return result.fail("保存失败");

        d_SysUser.setPasswordHash(PasswordUtil.encryBySHA256(rop.getNewPassword(),d_SysUser.getSecurityStamp()));

        if (sysUserMapper.update(d_SysUser) <= 0) {
            return result.fail("保存失败");
        }

        return result.success("保存成功");
    }
}
