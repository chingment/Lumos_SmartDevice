package com.caterbao.lumos.api.merch.controller;


import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.BorrowerService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrower")
public class BorrowerController extends  BaseController  {

    private BorrowerService borrowerService;

    @Autowired
    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> list(@RequestBody RopBorrowerList rop) {
        return borrowerService.list(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_add", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_add() {
        return borrowerService.init_add(this.getCurrentUserId(), this.getCurrentMerchId());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> add(@RequestBody RopBorrowerAdd rop) {
        return borrowerService.add(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_edit", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_edit(@RequestParam String id) {
        return borrowerService.init_edit(this.getCurrentUserId(), this.getCurrentMerchId(), id);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> edit(@RequestBody RopBorrowerEdit rop) {
        return borrowerService.edit(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }
}
