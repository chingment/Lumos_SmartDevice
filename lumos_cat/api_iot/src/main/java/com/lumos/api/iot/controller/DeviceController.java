package com.lumos.api.iot.controller;

import com.lumos.common.CustomResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @RequestMapping(value = "init", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init(){
        Map<String,Object> map=new HashMap<>();
        map.put("msg","是一个ssdddss人");

        //int a=1/0;
        return CustomResult.success("成功",map);
    }

}
