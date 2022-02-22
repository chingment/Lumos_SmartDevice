package com.caterbao.lumos.locals.biz.cache.impl;

import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.biz.cache.ProductCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheFactoryImpl implements CacheFactory {

    private ProductCacheService productCacheService;

    public ProductCacheService getProduct() {
        return productCacheService;
    }

    @Autowired
    private void setProductCacheService(ProductCacheService productCacheService) {
        this.productCacheService = productCacheService;
    }

}
