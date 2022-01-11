package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopMapper {
    Shop findOne(LumosSelective selective);
    List<Shop> find(LumosSelective selective);
    long insert(Shop shop);
    long update(Shop shop);
    long isExistName(String id,String merchId,String name);
}
