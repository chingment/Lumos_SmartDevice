package com.caterbao.lumos.api.device.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.caterbao.lumos.api.device.rop.RetDeviceInitData;
import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.api.device.service.DeviceService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DeviceServiceImpl implements DeviceService{


    public CustomResult init(String operater, String merchId, RopDeviceInitData rop) {

        if (rop == null)
            return CustomResult.fail("初始化数据对象为空");

        if(StringUtils.isEmpty(rop.getDeviceId()))
            return CustomResult.fail("设备编码为空");

        RetDeviceInitData ret =new RetDeviceInitData();



        HashMap<String,Object> customData=new HashMap<>();


        ret.setCumstonData(customData);

        return CustomResult.success("获取成功",ret);
    }
}
