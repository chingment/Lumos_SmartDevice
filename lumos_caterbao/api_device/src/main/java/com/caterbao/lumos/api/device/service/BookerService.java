package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;

public interface BookerService {

    CustomResult<RetBookerBorrowReturn> borrowReturn(String operater, RopBookerBorrowReturn rop);
    CustomResult<RetBookerBorrowReturnCreateFlow> borrowReturnCreateFlow(String operater, RopBookerBorrowReturnCreateFlow rop);
    CustomResult<RetBookerBorrowReturnOpenAction> borrowReturnOpenAction(String operater, RopBookerBorrowReturnOpenAction rop);
    CustomResult<RetBookerBorrowReturnCloseAction> borrowReturnCloseAction(String operater, RopBookerBorrowReturnCloseAction rop);
}
