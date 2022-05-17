package com.caterbao.lumos.api.device.controller;


import com.caterbao.lumos.api.device.rop.*;
import com.caterbao.lumos.api.device.service.BookerService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booker")
public class BookerController extends BaseController{

    private BookerService bookerService;

    @Autowired
    public BookerController(BookerService bookerService){
        this.bookerService=bookerService;
    }

    @RequestMapping(value = "createFlow", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetBookerCreateFlow> createFlow(@RequestBody RopBookerCreateFlow rop){
        return bookerService.createFlow(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "borrowReturn", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetBookerBorrowReturn> borrowReturn(@RequestBody RopBookerBorrowReturn rop){
        return bookerService.borrowReturn(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "sawBorrowBooks", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetBookerSawBorrowBooks> sawBorrowBooks(@RequestBody RopBookerSawBorrowBooks rop){
        return bookerService.sawBorrowBooks(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "renewBooks", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetBookerRenewBooks> renewBooks(@RequestBody RopBookerRenewBooks rop){
        return bookerService.renewBooks(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "displayBooks", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetBookerDisplayBooks> displayBooks(@RequestBody RopBookerDisplayBooks rop){
        return bookerService.displayBooks(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "takeStock", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetBookerTakeStock> takeStock(@RequestBody RopBookerTakeStock rop){
        return bookerService.takeStock(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "stockSlots", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetBookerStockSlots> stockSlots(@RequestBody RopBookerStockSlots rop){
        return bookerService.stockSlots(this.getCurrentUserId(),rop);
    }

}
