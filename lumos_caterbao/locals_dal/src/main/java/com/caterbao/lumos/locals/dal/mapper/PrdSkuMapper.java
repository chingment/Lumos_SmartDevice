package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.PrdSku;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdSkuMapper {
    long insert(PrdSku prdSku);
    long isExistCumCode(String id,String merchId,String cumCode);
}
