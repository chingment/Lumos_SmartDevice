package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.Shop;
import com.caterbao.lumos.locals.dal.pojo.Store;
import com.caterbao.lumos.locals.dal.pojo.StoreShop;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface StoreShopMapper {
    List<Shop> getBindShops(HashMap map);
    List<Shop> getUnBindShops(HashMap map);
    long isExist(StoreShop storeShop);
    long insert(StoreShop storeShop);
    long delete(StoreShop storeShop);
}
