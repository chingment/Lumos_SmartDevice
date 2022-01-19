package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopAdminUserAdd;
import com.caterbao.lumos.api.merch.rop.RopAdminUserEdit;
import com.caterbao.lumos.api.merch.rop.RopAdminUserList;
import com.caterbao.lumos.api.merch.service.AdminUserService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.ImgVo;
import com.caterbao.lumos.locals.common.PageResult;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.MerchDeviceMapper;
import com.caterbao.lumos.locals.dal.mapper.SysMerchUserMapper;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import com.caterbao.lumos.locals.dal.vw.MerchUserVw;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private SysMerchUserMapper sysMerchUserMapper;

    @Override
    public CustomResult init_add(String operater, String merchId) {
return  null;
    }

    @Override
    public CustomResult add(String operater, String merchId, RopAdminUserAdd rop) {
        return  null;
    }

    @Override
    public CustomResult list(String operater, String merchId, RopAdminUserList rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);

        List<MerchUserVw> d_MerchUsers = sysMerchUserMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (MerchUserVw d_MerchUser:
                d_MerchUsers ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_MerchUser.getId());
            item.put("fullName",d_MerchUser.getFullName());
            item.put("userName",d_MerchUser.getUserName());
            item.put("phoneNumber",d_MerchUser.getPhoneNumber());
            item.put("isDisable",d_MerchUser.isDisable());
            item.put("email",d_MerchUser.getEmail());
            item.put("createTime", CommonUtil.toDateTime(d_MerchUser.getCreateTime()));
            items.add(item);
        }

        long total = page.getTotal();
        PageResult<Object> ret = new PageResult<>();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return CustomResult.success("",ret);
    }

    @Override
    public CustomResult init_edit(String operater, String merchId, String shopId) {
        return  null;
    }

    @Override
    public CustomResult edit(String operater, String merchId, RopAdminUserEdit rop) {
        return  null;
    }
}
