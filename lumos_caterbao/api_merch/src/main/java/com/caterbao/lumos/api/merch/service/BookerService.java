package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopBookerBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBookerDeviceFeedback;
import com.caterbao.lumos.api.merch.rop.RopBookerRenewList;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface BookerService {
    CustomResult<Object> borrowList(String operater, String merchId, RopBookerBorrowList rop);
    CustomResult<Object> borrowDetails(String operater, String merchId, String flowDataId);
    CustomResult<Object> renewList(String operater, String merchId, RopBookerRenewList rop);
    CustomResult<Object> deviceFeedback(String operater, String merchId, RopBookerDeviceFeedback rop);
    CustomResult<Object> deviceFeedbackDetails(String operater, String merchId, String flowId);
}
