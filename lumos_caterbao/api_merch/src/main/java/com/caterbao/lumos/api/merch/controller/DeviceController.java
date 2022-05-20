package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.*;
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
    public CustomResult<Object> init_bookers() {
        return deviceService.init_bookers(this.getCurrentUserId(), this.getCurrentMerchId());
    }

    @RequestMapping(value = "bookers", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> bookers(@RequestBody RopDeviceBookers rop) {
        return deviceService.bookers(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_booker_manage", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_booker_manage(@RequestParam String id) {
        return deviceService.init_booker_manage(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "init_booker_baseinfo", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_booker_baseinfo(@RequestParam String id) {
        return deviceService.init_booker_baseinfo(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "init_booker_stock", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_booker_stock(@RequestParam String id) {
        return deviceService.init_booker_stock(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "booker_stock", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> booker_stock(@RequestBody RopDeviceBookerStock rop) {
        return deviceService.booker_stock(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> edit(@RequestBody RopDeviceEdit rop) {
        return deviceService.edit(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "rebootSys", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> rebootSys(@RequestBody RopDeviceRebootSys rop) {
        return deviceService.rebootSys(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "shutdownSys", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> shutdownSys(@RequestBody RopDeviceShutdownSys rop) {
        return deviceService.shutdownSys(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "updateApp", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> updateApp(@RequestBody RopDeviceUpdateApp rop) {
        return deviceService.updateApp(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }


}
