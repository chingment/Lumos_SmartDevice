package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ShopMapper {
    List<Shop> list(LumosSelective selective);
    long insert(Shop shop);
    long update(Shop shop);
    long isExistName(Shop shop);
    Shop findByShopId(LumosSelective selective);
}
