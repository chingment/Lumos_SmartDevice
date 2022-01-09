package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.MerchDevice;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MerchDeviceMapper {
    List<MerchDeviceVw> getBindDevices(LumosSelective selective);
    List<MerchDeviceVw> getUnBindDevices(LumosSelective selective);
    long bindOrUnBindDevice(MerchDevice merchDevice);
}
