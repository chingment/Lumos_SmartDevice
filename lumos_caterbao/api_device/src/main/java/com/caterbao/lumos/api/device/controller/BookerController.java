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


    @RequestMapping(value = "borrowreturn", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult<RetBookerBorrowReturn> borrowReturn(@RequestBody RopBookerBorrowReturn rop){
        return bookerService.borrowReturn(this.getCurrentUserId(),rop);
    }

}
