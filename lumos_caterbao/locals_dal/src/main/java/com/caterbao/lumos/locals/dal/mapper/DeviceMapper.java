package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.Device;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceMapper {
    Device findOne(LumosSelective selective);

    long update(Device device);
}
