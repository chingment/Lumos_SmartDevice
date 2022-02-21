package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopAdContents;
import com.caterbao.lumos.api.merch.rop.RopAdRelease;
import com.caterbao.lumos.api.merch.rop.RopAdSpaces;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface AdService {
    CustomResult<Object>  spaces(String operater, String merchId, RopAdSpaces rop);
    CustomResult<Object>  release(String operater, String merchId, RopAdRelease rop);
    CustomResult<Object>  contents(String operater, String merchId, RopAdContents rop);
}
