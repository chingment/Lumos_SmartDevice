package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopMapper {
    List<Shop> list();
    long insert(Shop shop);
    long update(Shop shop);
    Shop findByShopId(String shopId);
}
