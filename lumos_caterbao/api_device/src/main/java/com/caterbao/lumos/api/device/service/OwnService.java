package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.RopOwnGetInfo;
import com.caterbao.lumos.api.device.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.device.rop.RopOwnLogout;
import com.caterbao.lumos.api.device.rop.RopOwnSaveInfo;
import com.caterbao.lumos.locals.common.CustomResult;

public interface OwnService {
    CustomResult loginByAccount(RopOwnLoginByAccount rop);
    CustomResult logout(String operater,RopOwnLogout rop);
    CustomResult getInfo(String operater, RopOwnGetInfo rop);
    CustomResult saveInfo(String operater, RopOwnSaveInfo rop);
}
