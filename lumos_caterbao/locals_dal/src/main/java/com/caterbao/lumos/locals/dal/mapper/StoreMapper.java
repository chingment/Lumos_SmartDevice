package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.Store;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreMapper {
    Store findOne(LumosSelective selective);
    List<Store> find(LumosSelective selective);
}
