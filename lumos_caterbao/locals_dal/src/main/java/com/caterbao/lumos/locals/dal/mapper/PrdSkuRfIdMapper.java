package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.PrdSkuRfId;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdSkuRfIdMapper {
    PrdSkuRfId findOne(LumosSelective selective);
    long insert(PrdSkuRfId prdSkuRfId);
}
