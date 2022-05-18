package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookerTakeStockSheetItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookerTakeStockSheetItemMapper {
    long insertBatch(List<BookerTakeStockSheetItem> list);
    List<BookerTakeStockSheetItem> find(LumosSelective selective);
}
