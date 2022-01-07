package com.caterbao.lumos.api.merch.rop;

import com.caterbao.lumos.locals.common.BasePageRequest;

public class RopShopList extends BasePageRequest {

    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
