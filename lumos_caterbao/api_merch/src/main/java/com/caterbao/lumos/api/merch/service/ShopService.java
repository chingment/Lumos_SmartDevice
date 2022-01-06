package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopShopAdd;
import com.caterbao.lumos.api.merch.rop.RopShopEdit;
import com.caterbao.lumos.api.merch.rop.RopShopList;
import com.caterbao.lumos.locals.common.CustomResult;

public interface ShopService {
    CustomResult add(String operater, String merchId, RopShopAdd rop);
    CustomResult list(String operater, String merchId, RopShopList rop);
    CustomResult init_edit(String operater, String merchId, String shopId);
    CustomResult edit(String operater, String merchId, RopShopEdit rop);
}
