package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.SysMerchUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMerchUserMapper {
    SysMerchUser findByUserId(LumosSelective selective);
}
