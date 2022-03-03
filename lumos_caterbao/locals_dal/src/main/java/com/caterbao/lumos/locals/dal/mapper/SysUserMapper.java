package com.caterbao.lumos.locals.dal.mapper;


import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.SysMenu;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SysUserMapper {
    SysUser findOne(LumosSelective selective);
    List<SysMenu> getMenusByUserId(LumosSelective selective);
    long isExistUserName(String userName);
    long update(SysUser sysUser);
    long insert(SysUser sysUser);
    String getFullNameById(String id);
}
