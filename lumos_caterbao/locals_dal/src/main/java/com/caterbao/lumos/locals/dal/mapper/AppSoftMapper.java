package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.AdSpace;
import com.caterbao.lumos.locals.dal.pojo.AppSoft;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppSoftMapper {
    String getSecretByKey(String appKey);
    AppSoft findOne(LumosSelective selective);
}
