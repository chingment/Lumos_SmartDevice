package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.RopAdSpaces;
import com.caterbao.lumos.api.merch.rop.RopAdminUserList;
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
}
