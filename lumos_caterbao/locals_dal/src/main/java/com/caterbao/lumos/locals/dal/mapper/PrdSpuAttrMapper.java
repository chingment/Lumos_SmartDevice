package com.caterbao.lumos.locals.dal.mapper;


import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.PrdSpuAttr;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PrdSpuAttrMapper {
    long insert(PrdSpuAttr prdSpuAttr);
    long deleteBySpuId(String spuId);
    List<PrdSpuAttr> find(LumosSelective selective);
}
