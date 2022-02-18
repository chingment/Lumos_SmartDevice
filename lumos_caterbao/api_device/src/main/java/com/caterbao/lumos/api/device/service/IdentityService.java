package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.RopIdentityInfo;
import com.caterbao.lumos.api.device.rop.RopIdentityVerify;
import com.caterbao.lumos.locals.common.CustomResult;

public interface IdentityService {
    CustomResult verify(String operater, RopIdentityVerify rop);
    CustomResult info(String operater, RopIdentityInfo rop);
}
