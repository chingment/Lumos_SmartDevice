package com.lumos.api.merch.controller;


import com.lumos.common.CustomResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/own")

public class OwnController {

    @RequestMapping(value = "loginByAccount", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult loginByAccount(){
        Map<String,Object> map=new HashMap<>();
        map.put("token","123456");
        return CustomResult.success("成功",map);
    }

}
