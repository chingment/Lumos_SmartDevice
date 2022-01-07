package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.Store;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface StoreMapper {
    List<Store> list(HashMap map);
    long update(Store shop);
    Store findByStoreId(String storeId);
}
