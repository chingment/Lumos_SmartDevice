package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.BookerTakeStockSheetDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookerTakeStockSheetDetailMapper {
    long insertBatch(List<BookerTakeStockSheetDetail> list);
}
