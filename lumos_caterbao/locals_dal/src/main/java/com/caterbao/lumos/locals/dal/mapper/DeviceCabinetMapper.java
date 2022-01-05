package com.caterbao.lumos.locals.dal.mapper;


import com.caterbao.lumos.locals.dal.pojo.DeviceCabinet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceCabinetMapper {
    List<DeviceCabinet> findByDeviceId(String deviceId);
}
