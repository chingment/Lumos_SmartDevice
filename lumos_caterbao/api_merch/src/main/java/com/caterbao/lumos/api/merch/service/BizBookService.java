package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopBizBookBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBizBookFlowList;
import com.caterbao.lumos.api.merch.rop.RopBizBookRenewList;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface BizBookService {
    CustomResult<Object> borrowList(String operater, String merchId, RopBizBookBorrowList rop);
    CustomResult<Object> borrowDetails(String operater, String merchId, String borrowId);
    CustomResult<Object> renewList(String operater, String merchId, RopBizBookRenewList rop);
    CustomResult<Object> flowList(String operater, String merchId, RopBizBookFlowList rop);
    CustomResult<Object> flowDetails(String operater, String merchId, String flowId);
}
