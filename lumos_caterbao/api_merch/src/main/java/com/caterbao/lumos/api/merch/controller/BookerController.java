package com.caterbao.lumos.api.merch.controller;


import com.caterbao.lumos.api.merch.rop.RopBookerBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBookerFlowList;
import com.caterbao.lumos.api.merch.rop.RopBookerRenewList;
import com.caterbao.lumos.api.merch.service.BookerService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booker")
public class BookerController extends  BaseController  {

    private BookerService bookerService;

    @Autowired
    public BookerController(BookerService bookerService) {
        this.bookerService = bookerService;
    }

    @RequestMapping(value = "borrow/list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> borrowList(@RequestBody RopBookerBorrowList rop) {
        return bookerService.borrowList(this.getCurrentUserId(), this.getCurrentMerchId(),rop);
    }

    @RequestMapping(value = "borrow/details", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> borrowDetails(@RequestParam String id) {
        return bookerService.borrowDetails(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "flow/list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> flowList(@RequestBody RopBookerFlowList rop) {
        return bookerService.flowList(this.getCurrentUserId(), this.getCurrentMerchId(),rop);
    }

    @RequestMapping(value = "flow/details", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> flowDetails(@RequestParam String id) {
        return bookerService.flowDetails(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "renew/list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> renewList(@RequestBody RopBookerRenewList rop) {
        return bookerService.renewList(this.getCurrentUserId(), this.getCurrentMerchId(),rop);
    }



}
