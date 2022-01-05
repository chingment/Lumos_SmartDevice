package com.caterbao.lumos.locals.dal.mapper;

import com.caterbao.lumos.locals.dal.pojo.Device;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface DeviceMapper {
    Device firstOrDefault(HashMap<String,String> selective);
    long update(Device device);
}
