package com.caterbao.lumos.locals.dal.mapper;


import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookerSlot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookerSlotMapper {
    List<BookerSlot> find(LumosSelective selective);
    long update(BookerSlot bookerSlot);
}
