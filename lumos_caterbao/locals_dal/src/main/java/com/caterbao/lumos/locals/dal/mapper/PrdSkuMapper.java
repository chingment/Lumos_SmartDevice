package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.PrdSku;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdSkuMapper {
    List<PrdSku> find(LumosSelective selective);
    long insert(PrdSku prdSku);
    long isExistCumCode(String id,String merchId,String cumCode);
}
