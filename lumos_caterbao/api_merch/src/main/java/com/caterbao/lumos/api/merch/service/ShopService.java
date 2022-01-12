package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;

public interface ShopService {
    CustomResult add(String operater, String merchId, RopShopAdd rop);
    CustomResult list(String operater, String merchId, RopShopList rop);
    CustomResult details(String operater, String merchId, String shopId);
    CustomResult init_edit(String operater, String merchId, String shopId);
    CustomResult edit(String operater, String merchId, RopShopEdit rop);
    CustomResult devices(String operater, String merchId, RopShopDevices rop);
    CustomResult unDevices(String operater, String merchId, RopShopDevices rop);
    CustomResult bindDevice(String operater, String merchId, RopShopBindDevice rop);
    CustomResult unBindDevice(String operater, String merchId, RopShopBindDevice rop);
}
