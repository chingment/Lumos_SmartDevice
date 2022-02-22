package com.caterbao.lumos.locals.biz.cache;

import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.biz.model.SpuInfo;

public interface ProductCacheService {
    SkuInfo getSkuInfoByRfId(String merchId,String rfId);
    SkuInfo getSkuInfo(String merchId,String skuId);
    SpuInfo getSpuInfo(String merchId,String spuId);
    void removeSpuInfo(String merchId,String spuId);
}
