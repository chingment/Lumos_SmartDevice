package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.ShopDevice;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopDeviceMapper {
    ShopDevice findOne(LumosSelective selective);
    long count(LumosSelective selective);
    List<MerchDeviceVw> getBindDevices(LumosSelective selective);
    List<MerchDeviceVw> getUnBindDevices(LumosSelective selective);
    long insert(ShopDevice shopDevice);
    long update(ShopDevice shopDevice);
}
