package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopShopAdd;
import com.caterbao.lumos.api.merch.rop.RopShopList;
import com.caterbao.lumos.locals.common.CustomResult;

public interface ShopService {
    CustomResult add(String operater, String merchId, RopShopAdd rop);
}
