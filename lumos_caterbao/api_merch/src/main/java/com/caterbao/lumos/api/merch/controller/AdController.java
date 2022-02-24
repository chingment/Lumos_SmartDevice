package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.RopAdCreativeAdd;
import com.caterbao.lumos.api.merch.rop.RopAdCreativeEdit;
import com.caterbao.lumos.api.merch.rop.RopAdCreatives;
import com.caterbao.lumos.api.merch.rop.RopAdSpaces;
import com.caterbao.lumos.api.merch.service.AdService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ad")
public class AdController extends  BaseController  {

    private AdService adService;

    @Autowired
    public AdController(AdService adService) {
        this.adService = adService;
    }

    @RequestMapping(value = "spaces", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> spaces(@RequestBody RopAdSpaces rop) {
        return adService.spaces(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_creatives", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_creatives(@RequestParam String spaceId) {
        return adService.initCreatives(this.getCurrentUserId(), this.getCurrentMerchId(), spaceId);
    }

    @RequestMapping(value = "creatives", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> creatives(@RequestBody RopAdCreatives rop) {
        return adService.creatives(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_creative_add", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_creative_add(@RequestParam String spaceId) {
        return adService.initCreativeAdd(this.getCurrentUserId(), this.getCurrentMerchId(), spaceId);
    }


    @RequestMapping(value = "creativeAdd", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> creativeAdd(@RequestBody RopAdCreativeAdd rop) {
        return adService.creativeAdd(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_creative_edit", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_creative_edit(@RequestParam String id) {
        return adService.initCreativeEdit(this.getCurrentUserId(), this.getCurrentMerchId(), id);
    }


    @RequestMapping(value = "creativeEdit", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> creativeEdit(@RequestBody RopAdCreativeEdit rop) {
        return adService.creativeEdit(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }
}
