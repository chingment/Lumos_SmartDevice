package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;

public interface StoreService {

    CustomResult list(String operater, String merchId, RopStoreList rop);
    CustomResult init_manage(String operater, String merchId, String storeId);
    CustomResult init_manage_baseinfo(String operater, String merchId, String storeId);
    CustomResult shops(String operater, String merchId, RopStoreShops rop);
    CustomResult unShops(String operater, String merchId, RopStoreShops rop);
    CustomResult bindShop(String operater, String merchId, RopStoreBindShop rop);
    CustomResult unBindShop(String operater, String merchId, RopStoreBindShop rop);
}
