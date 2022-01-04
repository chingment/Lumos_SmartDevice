package com.caterbao.lumos.api.device.controller;

import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.service.OwnService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/own")
public class OwnController extends BaseController{

    private OwnService ownService;

    @Autowired
    public OwnController(OwnService ownService){
        this.ownService=ownService;
    }

    @RequestMapping(value = "loginByAccount", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult loginByAccount(@RequestBody RopOwnLoginByAccount rop){
        return ownService.loginByAccount(rop);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult logout(@RequestBody RopOwnLogout rop){
        return ownService.logout(this.getCurrentUserId(), rop);
    }

    @RequestMapping(value = "getInfo", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult getInfo(@RequestBody RopOwnGetInfo rop){
        return ownService.getInfo(this.getCurrentUserId(), rop);
    }

    @RequestMapping(value = "saveInfo", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult saveInfo(@RequestBody RopOwnSaveInfo rop){
        return ownService.saveInfo(this.getCurrentUserId(), rop);
    }
}
