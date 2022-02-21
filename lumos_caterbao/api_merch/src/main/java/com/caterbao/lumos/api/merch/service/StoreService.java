package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;

public interface StoreService {

    CustomResult<Object>  list(String operater, String merchId, RopStoreList rop);
    CustomResult<Object>  init_manage(String operater, String merchId, String storeId);
    CustomResult<Object>  init_manage_baseinfo(String operater, String merchId, String storeId);
    CustomResult<Object>  shops(String operater, String merchId, RopStoreShops rop);
    CustomResult<Object>  unShops(String operater, String merchId, RopStoreShops rop);
    CustomResult<Object>  bindShop(String operater, String merchId, RopStoreBindShop rop);
    CustomResult<Object>  unBindShop(String operater, String merchId, RopStoreBindShop rop);
}
