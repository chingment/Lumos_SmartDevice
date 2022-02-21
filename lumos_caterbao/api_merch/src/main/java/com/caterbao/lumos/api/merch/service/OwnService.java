package com.caterbao.lumos.api.merch.service;


import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.api.merch.rop.RopOwnLoginByAccount;

public interface OwnService {
    CustomResult<Object>  loginByAccount(RopOwnLoginByAccount rop);
    CustomResult<Object>  getInfo(String operater,String userId);
}
