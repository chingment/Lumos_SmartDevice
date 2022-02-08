package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopAdContents;
import com.caterbao.lumos.api.merch.rop.RopAdRelease;
import com.caterbao.lumos.api.merch.rop.RopAdSpaces;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface AdService {
    CustomResult spaces(String operater, String merchId, RopAdSpaces rop);
    CustomResult release(String operater, String merchId, RopAdRelease rop);
    CustomResult contents(String operater, String merchId, RopAdContents rop);
}
