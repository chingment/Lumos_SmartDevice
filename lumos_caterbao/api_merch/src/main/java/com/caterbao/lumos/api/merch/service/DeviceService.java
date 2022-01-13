package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopDeviceBookers;
import com.caterbao.lumos.api.merch.rop.RopProductList;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface DeviceService {
    CustomResult bookers(String operater, String merchId, RopDeviceBookers rop);
}
