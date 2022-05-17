package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;

public interface BookerService {
    CustomResult<RetBookerCreateFlow> createFlow(String operater, RopBookerCreateFlow rop);
    CustomResult<RetBookerBorrowReturn> borrowReturn(String operater, RopBookerBorrowReturn rop);
    CustomResult<RetBookerSawBorrowBooks> sawBorrowBooks(String operater, RopBookerSawBorrowBooks rop);
    CustomResult<RetBookerRenewBooks> renewBooks(String operater, RopBookerRenewBooks rop);
    CustomResult<RetBookerDisplayBooks> displayBooks(String operater, RopBookerDisplayBooks rop);
    CustomResult<RetBookerTakeStock> takeStock(String operater, RopBookerTakeStock rop);
    CustomResult<RetBookerStockSlots> stockSlots(String operater, RopBookerStockSlots rop);
    CustomResult<RetBookerStockInbound> stockInbound(String operater, RopBookerStockInbound rop);
}
