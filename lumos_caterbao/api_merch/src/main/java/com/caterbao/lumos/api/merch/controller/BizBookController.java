package com.caterbao.lumos.api.merch.controller;


import com.caterbao.lumos.api.merch.rop.RopBizBookBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBizBookFlowList;
import com.caterbao.lumos.api.merch.rop.RopBizBookRenewList;
import com.caterbao.lumos.api.merch.service.BizBookService;
import com.caterbao.lumos.api.merch.service.BookerService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bizbook")
public class BizBookController extends  BaseController  {

    private BizBookService bizBookService;

    @Autowired
    public BizBookController(BizBookService bizBookService) {
        this.bizBookService = bizBookService;
    }

    @RequestMapping(value = "borrow/list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> borrowList(@RequestBody RopBizBookBorrowList rop) {
        return bizBookService.borrowList(this.getCurrentUserId(), this.getCurrentMerchId(),rop);
    }

    @RequestMapping(value = "borrow/details", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> borrowDetails(@RequestParam String id) {
        return bizBookService.borrowDetails(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "flow/list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> flowList(@RequestBody RopBizBookFlowList rop) {
        return bizBookService.flowList(this.getCurrentUserId(), this.getCurrentMerchId(),rop);
    }

    @RequestMapping(value = "flow/details", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> flowDetails(@RequestParam String id) {
        return bizBookService.flowDetails(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "renew/list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> renewList(@RequestBody RopBizBookRenewList rop) {
        return bizBookService.renewList(this.getCurrentUserId(), this.getCurrentMerchId(),rop);
    }



}
