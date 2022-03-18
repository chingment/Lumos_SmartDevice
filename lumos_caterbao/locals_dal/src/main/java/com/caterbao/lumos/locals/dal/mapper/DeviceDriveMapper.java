package com.caterbao.lumos.locals.dal.mapper;


import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.pojo.DeviceDrive;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDriveMapper {
    List<DeviceDrive> find(LumosSelective selective);
}
