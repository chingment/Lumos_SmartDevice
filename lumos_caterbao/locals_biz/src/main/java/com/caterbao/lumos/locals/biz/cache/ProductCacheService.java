package com.caterbao.lumos.locals.biz.cache;

import com.caterbao.lumos.locals.biz.model.SkuInfo;

public interface ProductCacheService {
    SkuInfo getSkuInfoByRfId(String merchId,String rfId);
}
