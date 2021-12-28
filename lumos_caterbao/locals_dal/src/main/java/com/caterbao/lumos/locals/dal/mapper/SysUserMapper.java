package com.caterbao.lumos.locals.dal.mapper;


import com.caterbao.lumos.locals.dal.pojo.SysMenu;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SysUserMapper {

    SysUser findByUserNameAndPassword(String userName, String passwordHash);

    List<SysMenu> getMenusByUserId(String userId);
}
