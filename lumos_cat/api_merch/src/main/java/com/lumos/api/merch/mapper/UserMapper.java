package com.lumos.api.merch.mapper;

import com.lumos.api.merch.pojo.SysUser;
import com.lumos.api.merch.rop.RopOwnLoginByAccount;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper {

    SysUser findByUserNameAndPassword(String userName, String passwordHash);
}
