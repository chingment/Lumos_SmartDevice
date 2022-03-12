package com.caterbao.lumos.api.device.controller;


import com.caterbao.lumos.api.device.rop.RetDeviceInitData;
import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.api.device.service.DeviceService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController{

    private DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService){
        this.deviceService=deviceService;
    }

    @RequestMapping(value = "initData", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetDeviceInitData> initData(@RequestBody RopDeviceInitData rop){
        return deviceService.init(this.getCurrentUserId(),this.getCurrentMerchId(),rop);
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> test(){
        return new CustomResult<Object>();
    }

}
