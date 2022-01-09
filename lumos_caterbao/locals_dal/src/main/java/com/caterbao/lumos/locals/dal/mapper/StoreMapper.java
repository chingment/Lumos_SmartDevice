package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.Store;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface StoreMapper {
    List<Store> list(LumosSelective selective);
    long update(Store shop);
    Store findByStoreId(LumosSelective selective);
}
