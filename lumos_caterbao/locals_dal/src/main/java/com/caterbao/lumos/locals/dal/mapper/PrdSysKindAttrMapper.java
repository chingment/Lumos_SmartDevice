package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.PrdSysKind;
import com.caterbao.lumos.locals.dal.pojo.PrdSysKindAttr;
import com.caterbao.lumos.locals.dal.vw.PrdSysKindTreeVw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdSysKindAttrMapper {
    List<PrdSysKindAttr> find(LumosSelective selective);
}
