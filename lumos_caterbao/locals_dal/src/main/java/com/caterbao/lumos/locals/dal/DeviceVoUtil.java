package com.caterbao.lumos.locals.dal;

import com.alibaba.druid.util.StringUtils;

public class DeviceVoUtil {

    public  static String getCode(String id,String cumCode){
        if(StringUtils.isEmpty(cumCode))
            return id;
        return cumCode;
    }
}
