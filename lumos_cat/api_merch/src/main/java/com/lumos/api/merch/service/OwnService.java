package com.lumos.api.merch.service;


import com.lumos.api.merch.pojo.SysUser;
import com.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.lumos.common.CustomResult;

public interface OwnService {
    CustomResult loginByAccount(RopOwnLoginByAccount rop);
}
