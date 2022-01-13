package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProductList;
import com.caterbao.lumos.locals.common.CustomResult;

public interface ProductService {
    CustomResult list(String operater, String merchId, RopProductList rop);
    CustomResult add(String operater, String merchId, RopProdcutAdd rop);
}
