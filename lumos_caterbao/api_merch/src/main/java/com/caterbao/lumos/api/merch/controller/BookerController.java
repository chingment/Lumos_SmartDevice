package com.caterbao.lumos.api.merch.controller;


import com.caterbao.lumos.api.merch.rop.RopBookerBorrowList;
import com.caterbao.lumos.api.merch.rop.RopBookerDeviceFeedback;
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

    @RequestMapping(value = "renew/list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> renewList(@RequestBody RopBookerRenewList rop) {
        return bookerService.renewList(this.getCurrentUserId(), this.getCurrentMerchId(),rop);
    }

    @RequestMapping(value = "device/feedback", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> deviceFeedback(@RequestBody RopBookerDeviceFeedback rop) {
        return bookerService.deviceFeedback(this.getCurrentUserId(), this.getCurrentMerchId(),rop);
    }

}
