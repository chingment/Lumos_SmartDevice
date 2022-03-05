package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.RopOwnChangePassword;
import com.caterbao.lumos.api.merch.rop.RopOwnLogout;
import com.caterbao.lumos.locals.common.CustomResult;
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
    public CustomResult<Object> loginByAccount(@RequestBody RopOwnLoginByAccount rop) {
        return ownService.loginByAccount(rop);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> logout(RopOwnLogout rop){
        return  ownService.logout(getCurrentUserId(),rop);
    }

    @RequestMapping(value = "getInfo", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> getInfo(@RequestParam  String mode) {
        return ownService.getInfo(getCurrentUserId(), getCurrentUserId(), mode);
    }

    @RequestMapping(value = "checkPermission", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> checkPermission(RopOwnCheckPermission rop){
        CustomResult<Object> result = new CustomResult<>();
        return result.success("成功");
    }

    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> changePassword(@RequestBody RopOwnChangePassword rop){
        return  ownService.changePassword(getCurrentUserId(),rop);
    }

}
