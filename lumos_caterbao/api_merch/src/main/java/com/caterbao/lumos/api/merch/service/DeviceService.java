package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopDeviceEdit;
import com.caterbao.lumos.api.merch.rop.RopDeviceBookers;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface DeviceService {
    CustomResult init_bookers(String operater, String merchId);
    CustomResult bookers(String operater, String merchId, RopDeviceBookers rop);
    CustomResult init_manage(String operater, String merchId,String deviceId);
    CustomResult init_manage_baseinfo(String operater, String merchId,String deviceId);
    CustomResult edit(String operater, String merchId, RopDeviceEdit rop);

}

