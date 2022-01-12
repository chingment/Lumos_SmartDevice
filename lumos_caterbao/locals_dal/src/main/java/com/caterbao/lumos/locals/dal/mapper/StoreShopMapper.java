package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import com.caterbao.lumos.locals.dal.pojo.StoreShop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreShopMapper {
    StoreShop findOne(LumosSelective selective);
    List<Shop> getBindShops(LumosSelective selective);
    List<Shop> getUnBindShops(LumosSelective selective);
    long isExist(StoreShop storeShop);
    long insert(StoreShop storeShop);
    long update(StoreShop storeShop);
}
