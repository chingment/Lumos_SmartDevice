package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.RetDeviceCheckAppVersion;
import com.caterbao.lumos.api.device.rop.RetDeviceInitData;
import com.caterbao.lumos.api.device.rop.RopDeviceCheckAppVerion;
import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.locals.common.CustomResult;

public interface DeviceService {
    CustomResult<RetDeviceInitData> init(String operater, String merchId, RopDeviceInitData rop);
    CustomResult<RetDeviceCheckAppVersion> checkAppVerion(String operater,  RopDeviceCheckAppVerion rop);
}
