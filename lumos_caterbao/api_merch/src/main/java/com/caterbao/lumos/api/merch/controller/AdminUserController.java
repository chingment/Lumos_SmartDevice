package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.api.merch.rop.RopAdminUserAdd;
import com.caterbao.lumos.api.merch.rop.RopAdminUserEdit;
import com.caterbao.lumos.api.merch.rop.RopAdminUserList;
import com.caterbao.lumos.api.merch.service.AdminUserService;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/adminuser")
public class AdminUserController extends  BaseController  {

    private AdminUserService adminUserService;

    @Autowired
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult list(@RequestBody RopAdminUserList rop) {
        return adminUserService.list(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_add", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init_add() {
        return adminUserService.init_add(this.getCurrentUserId(), this.getCurrentMerchId());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult add(@RequestBody RopAdminUserAdd rop) {
        return adminUserService.add(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }

    @RequestMapping(value = "init_edit", method = RequestMethod.GET)
    @ResponseBody
    public CustomResult init_edit(@RequestParam String id) {
        return adminUserService.init_edit(this.getCurrentUserId(), this.getCurrentMerchId(), id);
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public CustomResult edit(@RequestBody RopAdminUserEdit rop) {
        return adminUserService.edit(this.getCurrentUserId(), this.getCurrentMerchId(), rop);
    }
}
