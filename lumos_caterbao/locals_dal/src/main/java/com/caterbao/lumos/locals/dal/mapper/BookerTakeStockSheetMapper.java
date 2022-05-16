package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookerStock;
import com.caterbao.lumos.locals.dal.pojo.BookerTakeStockSheet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookerTakeStockSheetMapper {
    BookerTakeStockSheet findLast(LumosSelective selective);
}
