package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.MerchDevice;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchDeviceMapper {
    MerchDeviceVw findOne(LumosSelective selective);
    List<MerchDeviceVw> find(LumosSelective selective);
    long count(LumosSelective selective);
    long isExistCumCode(String deviceId,String merchId,String cumCode);
    long update(MerchDevice merchDevice);
}
