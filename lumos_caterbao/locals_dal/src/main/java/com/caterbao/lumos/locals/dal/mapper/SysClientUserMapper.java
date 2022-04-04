package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.SysClientUser;
import com.caterbao.lumos.locals.dal.vw.SysClientUserVw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysClientUserMapper {
    SysClientUserVw findOne(LumosSelective selective);
    List<SysClientUserVw> find(LumosSelective selective);
    long insert(SysClientUser selective);
}
