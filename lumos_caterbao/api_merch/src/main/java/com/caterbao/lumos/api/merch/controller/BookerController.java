package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.*;
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

    @RequestMapping(value = "init_list", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_list() {
        return bookerService.init_list(this.getCurrentUserId(), this.getCurrentMerchId());
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> list(@RequestBody RopBookerList rop) {
        return bookerService.list(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_manage", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_manage(@RequestParam String id) {
        return bookerService.init_manage(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "init_baseinfo", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_baseinfo(@RequestParam String id) {
        return bookerService.init_baseinfo(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "init_stock", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult<Object> init_stock(@RequestParam String id) {
        return bookerService.init_stock(this.getCurrentUserId(), this.getCurrentMerchId(),id);
    }

    @RequestMapping(value = "stock", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> stock(@RequestBody RopBookerStock rop) {
        return bookerService.stock(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> edit(@RequestBody RopBookerEdit rop) {
        return bookerService.edit(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "rebootSys", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> rebootSys(@RequestBody RopBookerRebootSys rop) {
        return bookerService.rebootSys(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "shutdownSys", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> shutdownSys(@RequestBody RopBookerShutdownSys rop) {
        return bookerService.shutdownSys(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "updateApp", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<Object> updateApp(@RequestBody RopBookerUpdateApp rop) {
        return bookerService.updateApp(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }


}
