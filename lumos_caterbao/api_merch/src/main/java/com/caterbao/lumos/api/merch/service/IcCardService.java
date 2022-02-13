package com.caterbao.lumos.api.merch.service;


import com.caterbao.lumos.api.merch.rop.RopIcCardList;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface IcCardService {
    CustomResult list(String operater, String merchId, RopIcCardList rop);
}
