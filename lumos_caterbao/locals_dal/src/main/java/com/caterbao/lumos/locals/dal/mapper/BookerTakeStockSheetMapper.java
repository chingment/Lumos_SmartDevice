package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookerTakeStockSheet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookerTakeStockSheetMapper {
    BookerTakeStockSheet findOne(LumosSelective selective);
    BookerTakeStockSheet findLast(LumosSelective selective);
    long insert(BookerTakeStockSheet bookerTakeStockSheet);
    long update(BookerTakeStockSheet bookerTakeStockSheet);
}
