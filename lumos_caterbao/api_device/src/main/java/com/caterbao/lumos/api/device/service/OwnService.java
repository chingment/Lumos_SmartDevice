package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;

public interface OwnService {
    CustomResult<RetOwnLogin> loginByAccount(RopOwnLoginByAccount rop);
    CustomResult<RetOwnLogout> logout(String operater,RopOwnLogout rop);
    CustomResult<RetOwnGetInfo> getInfo(String operater, RopOwnGetInfo rop);
    CustomResult<RetOwnSaveInfo> saveInfo(String operater, RopOwnSaveInfo rop);
}
