package com.caterbao.lumos.api.device.controller;

import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.api.device.rop.RopIdentityBorrower;
import com.caterbao.lumos.api.device.rop.RopIdentityVerify;
import com.caterbao.lumos.api.device.service.IdentityService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity")
public class IdentityController extends BaseController{

    private IdentityService identityService;

    @Autowired
    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @RequestMapping(value = "verify", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult verify(@RequestBody RopIdentityVerify rop){
        return identityService.verify(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "borrower", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult borrower(@RequestBody RopIdentityBorrower rop) {
        return identityService.borrower(this.getCurrentUserId(), rop);
    }
}