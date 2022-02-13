package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.IcCard;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IcCardMapper {
    List<IcCard> find(LumosSelective selective);
}
