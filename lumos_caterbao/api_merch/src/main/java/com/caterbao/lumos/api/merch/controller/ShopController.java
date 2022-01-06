package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.RopShopAdd;
import com.caterbao.lumos.api.merch.rop.RopShopEdit;
import com.caterbao.lumos.api.merch.rop.RopShopList;
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
}
