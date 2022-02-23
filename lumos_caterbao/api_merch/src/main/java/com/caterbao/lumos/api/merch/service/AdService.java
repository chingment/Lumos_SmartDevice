package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopAdCreativeAdd;
import com.caterbao.lumos.api.merch.rop.RopAdCreatives;
import com.caterbao.lumos.api.merch.rop.RopAdSpaces;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface AdService {
    CustomResult<Object>  spaces(String operater, String merchId, RopAdSpaces rop);
    CustomResult<Object>  initCreatives(String operater, String merchId, String spaceId);
    CustomResult<Object>  creatives(String operater, String merchId, RopAdCreatives rop);
    CustomResult<Object>  initCreativeAdd(String operater, String merchId, String spaceId);
    CustomResult<Object>  creativeAdd(String operater, String merchId, RopAdCreativeAdd rop);
}
