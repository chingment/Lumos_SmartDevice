package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.PrdSpu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdSpuMapper {
    PrdSpu findOne(LumosSelective selective);
    List<PrdSpu> find(LumosSelective selective);
    long insert(PrdSpu prdSpu);
    long isExistCumCode(String id,String merchId,String cumCode);
}
