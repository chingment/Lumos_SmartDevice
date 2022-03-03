package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface BorrowerService {
    CustomResult<Object> list(String operater, String merchId, RopBorrowerList rop);
    CustomResult<Object> init_add(String operater, String merchId);
    CustomResult<Object>  add(String operater, String merchId, RopBorrowerAdd rop);
    CustomResult<Object>  init_edit(String operater, String merchId, String borrowerId);
    CustomResult<Object>  edit(String operater, String merchId, RopBorrowerEdit rop);
}
