package com.lumos.api.merch.controller;


import com.lumos.api.merch.rop.RetOwnGetInfo;
import com.lumos.api.merch.rop.RopOwnCheckPermission;
import com.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.lumos.api.merch.service.OwnService;
import com.lumos.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/own")

public class OwnController {

    @Autowired
    private OwnService ownService;

    @RequestMapping(value = "loginByAccount", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult loginByAccount(@RequestBody RopOwnLoginByAccount rop){
        return  ownService.loginByAccount(rop);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult logout(){
        return CustomResult.success("成功");
    }

    @RequestMapping(value = "getInfo", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult getInfo(){
        RetOwnGetInfo ret=new RetOwnGetInfo();
        ret.setUserName("chingment");
        return CustomResult.success("成功",ret);
    }

    @RequestMapping(value = "checkPermission", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult checkPermission(RopOwnCheckPermission rop){



        return CustomResult.success("成功");
    }

}
