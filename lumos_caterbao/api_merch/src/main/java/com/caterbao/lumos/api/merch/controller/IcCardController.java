package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.RopIcCardList;
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
    public CustomResult list(@RequestBody RopIcCardList rop) {
        return icCardService.list(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }
}
