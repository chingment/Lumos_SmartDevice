package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface DeviceService {
    CustomResult<Object>  init_bookers(String operater, String merchId);
    CustomResult<Object>  bookers(String operater, String merchId, RopDeviceBookers rop);
    CustomResult<Object>  init_booker_manage(String operater, String merchId,String deviceId);
    CustomResult<Object>  init_booker_baseinfo(String operater, String merchId,String deviceId);
    CustomResult<Object>  init_booker_stock(String operater, String merchId,String deviceId);
    CustomResult<Object>  booker_stock(String operater, String merchId, RopDeviceBookerStock rop);
    CustomResult<Object>  edit(String operater, String merchId, RopDeviceEdit rop);
    CustomResult<Object>  rebootSys(String operater, String merchId, RopDeviceRebootSys rop);
    CustomResult<Object>  shutdownSys(String operater, String merchId, RopDeviceShutdownSys rop);
    CustomResult<Object>  updateApp(String operater, String merchId, RopDeviceUpdateApp rop);
}

