package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.BookerStock;
import com.caterbao.lumos.locals.dal.vw.BookerDeviceSkuStockVw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookerStockMapper {
    List<BookerStock> find(LumosSelective selective);
    List<BookerDeviceSkuStockVw> findDevcieSkuStock(LumosSelective selective);
}
