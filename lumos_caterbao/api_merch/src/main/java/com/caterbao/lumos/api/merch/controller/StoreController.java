package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.StoreService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
public class StoreController extends  BaseController {

    private StoreService storeService;
    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult list(@RequestBody RopStoreList rop) {
        return storeService.list(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_manage", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init_manage(@RequestParam String id) {
        return storeService.init_manage(this.getCurrentUserId(), this.getCurrentMerchId(), id);
    }

    @RequestMapping(value = "init_manage_baseinfo", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init_manage_baseinfo(@RequestParam String id) {
        return storeService.init_manage_baseinfo(this.getCurrentUserId(), this.getCurrentMerchId(), id);
    }

    @RequestMapping(value = "shops", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult shops(@RequestBody RopStoreShops rop) {
        return storeService.shops(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "unShops", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult unShops(@RequestBody RopStoreShops rop) {
        return storeService.unShops(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "bindShop", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult bindShop(@RequestBody RopStoreBindShop rop) {
        return storeService.bindShop(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "unBindShop", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult unBindShop(@RequestBody RopStoreBindShop rop) {
        return storeService.unBindShop(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "devices", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult devices(@RequestBody RopStoreDevices rop) {
        return storeService.devices(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "unDevices", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult unDevices(@RequestBody RopStoreDevices rop) {
        return storeService.unDevices(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "bindDevice", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult bindDevice(@RequestBody RopStoreBindDevice rop) {
        return storeService.bindDevice(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "unBindDevice", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult unBindDevice(@RequestBody RopStoreBindDevice rop) {
        return storeService.unBindDevice(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }
}
