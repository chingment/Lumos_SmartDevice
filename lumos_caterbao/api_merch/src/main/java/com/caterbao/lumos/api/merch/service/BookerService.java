package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopBookerBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBookerFlowList;
import com.caterbao.lumos.api.merch.rop.RopBookerRenewList;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface BookerService {
    CustomResult<Object> borrowList(String operater, String merchId, RopBookerBorrowList rop);
    CustomResult<Object> borrowDetails(String operater, String merchId, String borrowId);
    CustomResult<Object> renewList(String operater, String merchId, RopBookerRenewList rop);
    CustomResult<Object> flowList(String operater, String merchId, RopBookerFlowList rop);
    CustomResult<Object> flowDetails(String operater, String merchId, String flowId);
}
