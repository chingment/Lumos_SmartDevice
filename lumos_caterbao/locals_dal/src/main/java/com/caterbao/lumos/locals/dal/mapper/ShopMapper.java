package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.Shop;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopMapper {
    long insert(Shop shop);
}
