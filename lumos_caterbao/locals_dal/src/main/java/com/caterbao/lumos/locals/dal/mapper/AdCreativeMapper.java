package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.AdCreative;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdCreativeMapper {
    AdCreative findOne(LumosSelective selective);
    List<AdCreative> find(LumosSelective selective);
    long insert(AdCreative adCreative);
    long update(AdCreative adCreative);
}
