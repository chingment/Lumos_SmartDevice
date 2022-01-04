package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.RopOwnGetInfo;
import com.caterbao.lumos.api.device.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.device.rop.RopOwnLogout;
import com.caterbao.lumos.api.device.rop.RopOwnSaveInfo;
import com.caterbao.lumos.api.device.service.OwnService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public class OwnServiceImpl implements OwnService {

    @Override
    public CustomResult loginByAccount(RopOwnLoginByAccount rop) {
        return  null;
    }

    @Override
    public CustomResult logout(String operater, RopOwnLogout rop) {
        return  null;
    }

    @Override
    public CustomResult getInfo(String operater, RopOwnGetInfo rop) {
        return  null;
    }

    @Override
    public CustomResult saveInfo(String operater, RopOwnSaveInfo rop) {
        return  null;
    }
}
