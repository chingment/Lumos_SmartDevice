package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.Device;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface DeviceMapper {
    Device findByDeviceId(LumosSelective selective);
    long update(Device device);
}
