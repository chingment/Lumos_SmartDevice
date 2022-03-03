package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.IcCardService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iccard")
public class IcCardController extends  BaseController  {

    private IcCardService icCardService;

    @Autowired
    public IcCardController(IcCardService icCardService) {
        this.icCardService = icCardService;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> list(@RequestBody RopIcCardList rop) {
        return icCardService.list(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_add", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_add() {
        return icCardService.init_add(this.getCurrentUserId(), this.getCurrentMerchId());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> add(@RequestBody RopIcCardAdd rop) {
        return icCardService.add(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_edit", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_edit(@RequestParam String id) {
        return icCardService.init_edit(this.getCurrentUserId(), this.getCurrentMerchId(), id);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> edit(@RequestBody RopIcCardEdit rop) {
        return icCardService.edit(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }
}
