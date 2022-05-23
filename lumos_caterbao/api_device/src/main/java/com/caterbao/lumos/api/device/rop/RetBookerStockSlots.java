package com.caterbao.lumos.api.device.rop;

import com.caterbao.lumos.locals.common.PageResult;

public class RetBookerStockSlots extends PageResult<Object> {

    private long stockQuantity;

    public long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
