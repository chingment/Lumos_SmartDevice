package com.caterbao.lumos.locals.dal.mapper;


import com.caterbao.lumos.locals.dal.pojo.SysUser;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper {

    SysUser findByUserNameAndPassword(String/**/ userName, String passwordHash);
}
