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
    long deleteDeviceStockByDeviceId(String merchId,String storeId,String shopId,String deviceId);
    long deleteDeviceStockBySlotId(String merchId,String storeId,String shopId,String deviceId,String slotId);
    long deleteDeviceStockBySkuRfId(String merchId,String storeId,String shopId,String deviceId,String skuRfId);
    long insertBatch(List<BookerStock> list);
    long insert(BookerStock bookerStock);
    long getDeviceStockQuantityByDeviceId(String merchId,String storeId,String shopId,String deviceId);
    long getDeviceStockQuantityBySlotId(String merchId,String storeId,String shopId,String deviceId,String slotId);
    long getDeviceStockQuantityBySkuRfId(String merchId,String storeId,String shopId,String deviceId,String skuRfId);
}
