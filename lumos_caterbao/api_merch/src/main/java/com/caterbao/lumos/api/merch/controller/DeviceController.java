package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.RopDeviceBookers;
import com.caterbao.lumos.api.merch.service.DeviceService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class DeviceController extends  BaseController  {

    private DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @RequestMapping(value = "init_bookers", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init_bookers() {
        return deviceService.init_bookers(this.getCurrentUserId(), this.getCurrentMerchId());
    }

    @RequestMapping(value = "bookers", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult bookers(@RequestBody RopDeviceBookers rop) {
        return deviceService.bookers(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_manage", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init_manage(@RequestParam String id) {
        return deviceService.init_manage(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "init_manage_baseinfo", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init_manage_baseinfo(@RequestParam String id) {
        return deviceService.init_manage_baseinfo(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

}
