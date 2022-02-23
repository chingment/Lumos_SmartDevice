package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.AdSpace;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdSpaceMapper {
    AdSpace findOne(LumosSelective selective);
    List<AdSpace> find(LumosSelective selective);
}
