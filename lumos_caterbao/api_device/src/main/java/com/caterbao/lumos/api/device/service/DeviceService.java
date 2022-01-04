package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.locals.common.CustomResult;

public interface DeviceService {
    CustomResult init(String operater, String merchId, RopDeviceInitData rop);
}
