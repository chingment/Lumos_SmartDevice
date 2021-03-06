package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.RetIdentityInfo;
import com.caterbao.lumos.api.device.rop.RetIdentityVerify;
import com.caterbao.lumos.api.device.rop.RopIdentityInfo;
import com.caterbao.lumos.api.device.rop.RopIdentityVerify;
import com.caterbao.lumos.locals.common.CustomResult;

public interface IdentityService {
    CustomResult<RetIdentityVerify> verify(String operater, RopIdentityVerify rop);
    CustomResult<RetIdentityInfo> info(String operater, RopIdentityInfo rop);
}
