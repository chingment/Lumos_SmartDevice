package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.ShopService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ShopController extends  BaseController {

    private ShopService shopService;
    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult list(@RequestBody RopShopList rop) {
        return shopService.list(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "details", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult details(@RequestParam String id) {
        return shopService.details(this.getCurrentUserId(), this.getCurrentMerchId(), id);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult add(@RequestBody RopShopAdd rop) {
        return shopService.add(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_edit", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init_edit(@RequestParam String id) {
        return shopService.init_edit(this.getCurrentUserId(), this.getCurrentMerchId(), id);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult edit(@RequestBody RopShopEdit rop) {
        return shopService.edit(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "devices", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult devices(@RequestBody RopShopDevices rop) {
        return shopService.devices(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "unDevices", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult unDevices(@RequestBody RopShopDevices rop) {
        return shopService.unDevices(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "bindDevice", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult bindDevice(@RequestBody RopShopBindDevice rop) {
        return shopService.bindDevice(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "unBindDevice", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult unBindDevice(@RequestBody RopShopBindDevice rop) {
        return shopService.unBindDevice(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }
}
