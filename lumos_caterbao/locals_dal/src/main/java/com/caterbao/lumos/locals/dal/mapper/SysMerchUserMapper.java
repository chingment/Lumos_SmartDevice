package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.SysMerchUser;
import com.caterbao.lumos.locals.dal.vw.MerchUserVw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMerchUserMapper {
    SysMerchUser findOne(LumosSelective selective);
    List<MerchUserVw> find(LumosSelective selective);
}
