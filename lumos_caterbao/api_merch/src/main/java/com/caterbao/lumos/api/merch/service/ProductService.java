package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopProductListBySpu;
import com.caterbao.lumos.locals.common.CustomResult;

public interface ProductService {
    CustomResult listBySpu(String operater,String merchId,RopProductListBySpu rop);
}