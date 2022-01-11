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
    long update(SysUser sysUser);
}
