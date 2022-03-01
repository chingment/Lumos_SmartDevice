package com.caterbao.lumos.api.merch.controller;


import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProdcutDelete;
import com.caterbao.lumos.api.merch.rop.RopProdcutEdit;
import com.caterbao.lumos.api.merch.rop.RopProductList;
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

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> list(@RequestBody RopProductList rop) {
        return productService.list(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_add", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_add() {
        return productService.init_add(this.getCurrentUserId(), this.getCurrentMerchId());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> add(@RequestBody RopProdcutAdd rop) {
        return productService.add(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_edit", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_edit(@RequestParam String id) {
        return productService.init_edit(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> add(@RequestBody RopProdcutEdit rop) {
        return productService.edit(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> delete(@RequestBody RopProdcutDelete rop) {
        return productService.delete(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "getSysKindAttrs", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> getSysKindAttrs(@RequestParam String ids) {
        return productService.getSysKindAttrs(this.getCurrentUserId(), this.getCurrentMerchId(), ids);
    }
}
