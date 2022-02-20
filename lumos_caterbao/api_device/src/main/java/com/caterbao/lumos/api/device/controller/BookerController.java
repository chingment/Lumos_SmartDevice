package com.caterbao.lumos.api.device.controller;


import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturnCloseAction;
import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturnCreateFlow;
import com.caterbao.lumos.api.device.rop.RopBookerBorrowReturnOpenAction;
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

    @RequestMapping(value = "borrowreturn/createflow", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult borrowReturnCreateflow(@RequestBody RopBookerBorrowReturnCreateFlow rop){
        return bookerService.borrowReturnCreateFlow(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "borrowreturn/openaction", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult borrowReturnOpenAction(@RequestBody RopBookerBorrowReturnOpenAction rop){
        return bookerService.borrowReturnOpenAction(this.getCurrentUserId(),rop);
    }

    @RequestMapping(value = "borrowreturn/closeaction", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult borrowReturnCloseAction(@RequestBody RopBookerBorrowReturnCloseAction rop){
        return bookerService.borrowReturnCloseAction(this.getCurrentUserId(),rop);
    }
}
