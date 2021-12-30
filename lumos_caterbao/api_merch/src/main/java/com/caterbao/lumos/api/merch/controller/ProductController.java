package com.caterbao.lumos.api.merch.controller;


import com.caterbao.lumos.api.merch.rop.RopProductListBySpu;
import com.caterbao.lumos.api.merch.service.ProductService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController extends  BaseController {


    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "listBySpu", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult listBySpu(@RequestBody RopProductListBySpu rop) {
        return productService.listBySpu(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }
}
