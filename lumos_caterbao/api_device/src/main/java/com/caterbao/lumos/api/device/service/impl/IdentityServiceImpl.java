package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.RopIdentityBorrower;
import com.caterbao.lumos.api.device.rop.RopIdentityVerify;
import com.caterbao.lumos.api.device.service.IdentityService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public class IdentityServiceImpl implements IdentityService {

    @Override
    public CustomResult verify(String operater, RopIdentityVerify rop) {

        return null;
    }

    @Override
    public CustomResult borrower(String operater, RopIdentityBorrower rop) {

        return null;
    }

}
