package com.caterbao.lumos.api.merch.service;


import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface IcCardService {
    CustomResult<Object>  list(String operater, String merchId, RopIcCardList rop);
    CustomResult<Object> init_add(String operater, String merchId);
    CustomResult<Object>  add(String operater, String merchId, RopIcCardAdd rop);
    CustomResult<Object>  init_edit(String operater, String merchId, String userId);
    CustomResult<Object>  edit(String operater, String merchId, RopIcCardEdit rop);
}
