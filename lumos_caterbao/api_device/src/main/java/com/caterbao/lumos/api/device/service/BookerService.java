package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturnCloseAction;
import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturnCreateFlow;
import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturnOpenAction;
import com.caterbao.lumos.locals.common.CustomResult;

public interface BookerService {
    CustomResult borrowReturnCreateFlow(String operater, RopBookerBorrowReturnCreateFlow rop);
    CustomResult borrowReturnOpenAction(String operater, RopBookerBorrowReturnOpenAction rop);
    CustomResult borrowReturnCloseAction(String operater, RopBookerBorrowReturnCloseAction rop);
}
