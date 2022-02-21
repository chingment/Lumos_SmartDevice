package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;

public interface ShopService {
    CustomResult<Object>  add(String operater, String merchId, RopShopAdd rop);
    CustomResult<Object>  list(String operater, String merchId, RopShopList rop);
    CustomResult<Object>  details(String operater, String merchId, String shopId);
    CustomResult<Object>  init_edit(String operater, String merchId, String shopId);
    CustomResult<Object>  edit(String operater, String merchId, RopShopEdit rop);
    CustomResult<Object>  devices(String operater, String merchId, RopShopDevices rop);
    CustomResult<Object>  unDevices(String operater, String merchId, RopShopDevices rop);
    CustomResult<Object>  bindDevice(String operater, String merchId, RopShopBindDevice rop);
    CustomResult<Object>  unBindDevice(String operater, String merchId, RopShopBindDevice rop);
}
