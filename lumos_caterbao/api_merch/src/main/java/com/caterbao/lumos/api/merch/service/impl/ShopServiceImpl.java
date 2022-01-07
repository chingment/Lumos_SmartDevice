package com.caterbao.lumos.api.merch.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.caterbao.lumos.api.merch.rop.RopShopAdd;
import com.caterbao.lumos.api.merch.rop.RopShopEdit;
import com.caterbao.lumos.api.merch.rop.RopShopList;
import com.caterbao.lumos.api.merch.service.ShopService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.mapper.ShopMapper;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public CustomResult list(String operater, String merchId, RopShopList rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page =PageHelper.startPage(pageNum, pageSize);

        HashMap<String,String> selective=new HashMap<>();
        selective.put("fields","id,name,displayImgUrls");
        selective.put("where_name",rop.getShopName());
        selective.put("where_merchId",merchId);
        List<Shop> d_Shops = shopMapper.list(selective);

        List<Object> items=new ArrayList<>();

        for (Shop d_Shop:
                d_Shops ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_Shop.getId());
            item.put("name",d_Shop.getName());
            item.put("imgUrl", ImgVo.getMainImgUrl(d_Shop.getDisplayImgUrls()));
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
    public CustomResult add(String operater, String merchId, RopShopAdd rop) {

        if(StringUtils.isEmpty(merchId))
            return CustomResult.fail("商户编号不能为空");

        if(StringUtils.isEmpty(rop.getName()))
            return CustomResult.fail("名称不能为空");

        Shop d_Shop = new Shop();
        d_Shop.setMerchId(merchId);
        d_Shop.setName(rop.getName());

        if(shopMapper.isExistName(d_Shop)>0)
            return CustomResult.fail("名称已经存在");

        d_Shop.setId(IdWork.generateGUID());
        d_Shop.setContactName(rop.getContactName());
        d_Shop.setContactPhone(rop.getContactPhone());
        d_Shop.setContactAddress(rop.getContactAddress());
        d_Shop.setDisplayImgUrls(JsonUtil.getJson(rop.getDisplayImgUrls()));
        d_Shop.setCreator(operater);
        d_Shop.setCreateTime(CommonUtil.getDateTimeNow());

        long isFlag = shopMapper.insert(d_Shop);

        if(isFlag>0)
            return CustomResult.success("保存成功");

        return CustomResult.success("保存失败");
    }

    @Override
    public CustomResult init_edit(String operater, String merchId, String shopId) {

        Shop d_Shop = shopMapper.findByShopId(shopId);

        if(d_Shop==null)
           return CustomResult.fail("初始失败");

        HashMap<String,Object> ret=new HashMap<>();

        ret.put("id",d_Shop.getId());
        ret.put("name",d_Shop.getName());

        ret.put("displayImgUrls",JsonUtil.toObject(d_Shop.getDisplayImgUrls()));
        ret.put("contactName",d_Shop.getContactName());
        ret.put("contactPhone",d_Shop.getContactPhone());
        ret.put("contactAddress",d_Shop.getContactAddress());

        return CustomResult.success("初始成功",ret);
    }

    @Override
    public CustomResult edit(String operater, String merchId, RopShopEdit rop) {

        Shop d_Shop = shopMapper.findByShopId(rop.getId());

        if (d_Shop == null)
            return CustomResult.fail("数据不存在");


        d_Shop.setName(rop.getName());
        d_Shop.setMerchId(merchId);

        if(shopMapper.isExistName(d_Shop)>0)
            return CustomResult.fail("名称已经存在");

        d_Shop.setDisplayImgUrls(JsonUtil.getJson(rop.getDisplayImgUrls()));

        d_Shop.setContactName(rop.getContactName());
        d_Shop.setContactPhone(rop.getContactPhone());
        d_Shop.setContactAddress(rop.getContactAddress());
        d_Shop.setMender(operater);
        d_Shop.setMendTime(CommonUtil.getDateTimeNow());

        long isflag = shopMapper.update(d_Shop);
        if (isflag>0)
            return CustomResult.success("保存成功");

        return CustomResult.success("保存失败");
    }
}
