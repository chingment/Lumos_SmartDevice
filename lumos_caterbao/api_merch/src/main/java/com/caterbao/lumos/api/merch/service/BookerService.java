package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface BookerService {
    CustomResult<Object>  init_list(String operater, String merchId);
    CustomResult<Object>  list(String operater, String merchId, RopBookerList rop);
    CustomResult<Object>  init_manage(String operater, String merchId,String deviceId);
    CustomResult<Object>  init_baseinfo(String operater, String merchId,String deviceId);
    CustomResult<Object>  init_stock(String operater, String merchId,String deviceId);
    CustomResult<Object>  stock(String operater, String merchId, RopBookerStock rop);
    CustomResult<Object>  edit(String operater, String merchId, RopBookerEdit rop);
    CustomResult<Object>  rebootSys(String operater, String merchId, RopBookerRebootSys rop);
    CustomResult<Object>  shutdownSys(String operater, String merchId, RopBookerShutdownSys rop);
    CustomResult<Object>  updateApp(String operater, String merchId, RopBookerUpdateApp rop);
}
