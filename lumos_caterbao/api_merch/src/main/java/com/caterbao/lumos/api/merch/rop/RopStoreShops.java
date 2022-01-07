package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

public class RopStoreShops extends BasePageRequest {
    private String storeId;
    private String shopName;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
