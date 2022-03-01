package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.PrdSysKind;
import com.caterbao.lumos.locals.dal.vw.PrdSysKindTreeVw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdSysKindMapper {
    List<PrdSysKindTreeVw> tree();

    PrdSysKind find(LumosSelective selective);

    List<PrdSysKind> findParentById(int id);
}
