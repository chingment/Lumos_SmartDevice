package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.SysMerchUser;
import com.caterbao.lumos.locals.dal.vw.SysMerchUserVw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMerchUserMapper {
    SysMerchUserVw findOne(LumosSelective selective);
    List<SysMerchUserVw> find(LumosSelective selective);
    long insert(SysMerchUser sysMerchUser);
}
