package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.IcCard;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IcCardMapper {
    List<IcCard> find(LumosSelective selective);
    IcCard findOne(LumosSelective selective);
    long isExistCardNo(String id,String merchId,String cardNo);
    long insert(IcCard icCard);
    String getFullNameById(String id);
}
