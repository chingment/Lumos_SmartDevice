package com.caterbao.lumos.api.device.service;

import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturn;
import com.caterbao.lumos.locals.common.CustomResult;

public interface BookerService {
    CustomResult borrowReturn(String operater, RopBookerBorrowReturn rop);
}
