package com.caterbao.lumos.api.merch.controller;


import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.api.merch.rop.RetOwnGetInfo;
import com.caterbao.lumos.api.merch.rop.RopOwnCheckPermission;
import com.caterbao.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.merch.service.OwnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/own")
public class OwnController extends  BaseController {

    private OwnService ownService;

    @Autowired
    public OwnController(OwnService ownService) {
        this.ownService = ownService;
    }

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
    public CustomResult getInfo() {
        String useriD = getCurrentUserId();
        System.out.println("useriD:"+useriD);
        RetOwnGetInfo ret = new RetOwnGetInfo();
        ret.setUserName("chingment");
        return CustomResult.success("成功ssdddrrrddddd", ret);
    }

    @RequestMapping(value = "checkPermission", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult checkPermission(RopOwnCheckPermission rop){



        return CustomResult.success("成功");
    }

}
