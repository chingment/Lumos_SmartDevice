package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.RopProductListBySpu;
import com.caterbao.lumos.api.merch.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.api.merch.rop.RetOwnGetInfo;
import com.caterbao.lumos.api.merch.rop.RopOwnCheckPermission;
import com.caterbao.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.merch.service.OwnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/own")
public class OwnController extends  BaseController {


    private OwnService ownService;

    private ProductService productService;
    @Autowired
    public OwnController(OwnService ownService,ProductService productService) {
        this.ownService = ownService;
        this.productService=productService;
    }

    @RequestMapping(value = "loginByAccount", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult loginByAccount(@RequestBody RopOwnLoginByAccount rop) {

//        RopProductListBySpu rp = new RopProductListBySpu();
//        rp.setPageNum(1);
//        rp.setPageSize(10);
//        productService.listBySpu("", "", rp);
        return ownService.loginByAccount(rop);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult logout(){
        return CustomResult.success("成功");
    }

    @RequestMapping(value = "getInfo", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult getInfo() {
        return  ownService.getInfo(getCurrentUserId(),getCurrentUserId());
    }

    @RequestMapping(value = "checkPermission", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult checkPermission(RopOwnCheckPermission rop){

        return CustomResult.success("成功");
    }

}
