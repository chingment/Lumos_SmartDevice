package com.lumos.api.merch.controller;


import com.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.lumos.common.CustomResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/own")

public class OwnController {

    @RequestMapping(value = "loginByAccount", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult loginByAccount(@RequestBody RopOwnLoginByAccount rop){
        System.out.print("getUsername."+rop.getUsername());
        System.out.print("getPassword."+rop.getPassword());
        Map<String,Object> map=new HashMap<>();
        map.put("token","123456");
        return CustomResult.success("成功",map);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult logout(){
        return CustomResult.success("成功");
    }

    @RequestMapping(value = "getInfo", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult getInfo(){
        return CustomResult.success("成功");
    }

}
