package com.caterbao.lumos.api.merch.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.ShopService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.ShopDeviceMapper;
import com.caterbao.lumos.locals.dal.mapper.ShopMapper;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import com.caterbao.lumos.locals.dal.pojo.ShopDevice;
import com.caterbao.lumos.locals.dal.pojo.StoreShop;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private ShopDeviceMapper shopDeviceMapper;

    @Override
    public CustomResult list(String operater, String merchId, RopShopList rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page =PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,DisplayImgUrls");
        selective.addWhere("Name",rop.getShopName());
        selective.addWhere("MerchId",merchId);

        List<Shop> d_Shops = shopMapper.find(selective);

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
    public CustomResult details(String operater, String merchId, String shopId) {

        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,DisplayImgUrls,ContactName,ContactPhone,ContactAddress");
        selective.addWhere("ShopId",shopId);
        selective.addWhere("MerchId",merchId);

        Shop d_Shop = shopMapper.findOne(selective);

        HashMap<String,Object> ret=new HashMap<>();

        ret.put("id",d_Shop.getId());
        ret.put("name",d_Shop.getName());

        ret.put("displayImgUrls",JsonUtil.toObject(d_Shop.getDisplayImgUrls()));
        ret.put("contactName",d_Shop.getContactName());
        ret.put("contactPhone",d_Shop.getContactPhone());
        ret.put("contactAddress",d_Shop.getContactAddress());

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

        if(shopMapper.isExistName(null,merchId,rop.getName())>0)
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

        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,DisplayImgUrls,ContactName,ContactPhone,ContactAddress");
        selective.addWhere("ShopId",shopId);
        selective.addWhere("MerchId",merchId);

        Shop d_Shop = shopMapper.findOne(selective);

        if (d_Shop == null)
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

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("ShopId",rop.getId());
        selective.addWhere("MerchId",merchId);

        Shop d_Shop = shopMapper.findOne(selective);

        if (d_Shop == null)
            return CustomResult.fail("数据不存在");


        d_Shop.setName(rop.getName());
        d_Shop.setMerchId(merchId);

        if(shopMapper.isExistName(d_Shop.getId(),merchId,rop.getName())>0)
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

    @Override
    public CustomResult devices(String operater, String merchId, RopShopDevices rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        Page<?> page =PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("StoreId",rop.getStoreId());
        selective.addWhere("ShopId",rop.getShopId());
        selective.addWhere("DeviceCode",rop.getDeviceCode());

        List<MerchDeviceVw> d_Devices = shopDeviceMapper.getBindDevices(selective);

        List<Object> items=new ArrayList<>();

        for (MerchDeviceVw d_Device:
                d_Devices ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_Device.getId());
            item.put("cumCode", d_Device.getCumCode());

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
    public CustomResult unDevices(String operater, String merchId, RopShopDevices rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        Page<?> page =PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective=new LumosSelective();
        selective.addWhere("MerchId",merchId);
        selective.addWhere("StoreId",rop.getStoreId());
        selective.addWhere("ShopId",rop.getShopId());
        selective.addWhere("DeviceCode",rop.getDeviceCode());

        List<MerchDeviceVw> d_Devices = shopDeviceMapper.getUnBindDevices(selective);

        List<Object> items=new ArrayList<>();

        for (MerchDeviceVw d_Device:
                d_Devices ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_Device.getId());
            item.put("cumCode", d_Device.getCumCode());
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
    @Transactional
    public CustomResult bindDevice(String operater, String merchId, RopShopBindDevice rop) {

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("StoreId",rop.getStoreId());
        selective.addWhere("ShopId",rop.getShopId());
        selective.addWhere("DeviceId",rop.getDeviceId());

        ShopDevice d_ShopDevice=shopDeviceMapper.findOne(selective);

        if(d_ShopDevice==null) {
            d_ShopDevice = new ShopDevice();
            d_ShopDevice.setId(IdWork.generateGUID());
            d_ShopDevice.setMerchId(merchId);
            d_ShopDevice.setStoreId(rop.getStoreId());
            d_ShopDevice.setShopId(rop.getShopId());
            d_ShopDevice.setDeviceId(rop.getDeviceId());
            d_ShopDevice.setBindStatus(1);
            d_ShopDevice.setCreator(operater);
            d_ShopDevice.setCreateTime(CommonUtil.getDateTimeNow());
            shopDeviceMapper.insert(d_ShopDevice);
        }
        else {
            d_ShopDevice.setBindStatus(1);
            d_ShopDevice.setMender(operater);
            d_ShopDevice.setMendTime(CommonUtil.getDateTimeNow());
            shopDeviceMapper.update(d_ShopDevice);
        }

        return CustomResult.success("绑定成功");

    }

    @Override
    @Transactional
    public CustomResult unBindDevice(String operater, String merchId, RopShopBindDevice rop) {

        ShopDevice d_ShopDevice = new ShopDevice();
        d_ShopDevice.setMerchId(merchId);
        d_ShopDevice.setStoreId(rop.getStoreId());
        d_ShopDevice.setShopId(rop.getShopId());
        d_ShopDevice.setDeviceId(rop.getDeviceId());
        d_ShopDevice.setBindStatus(2);
        d_ShopDevice.setMender(operater);
        d_ShopDevice.setMendTime(CommonUtil.getDateTimeNow());
        shopDeviceMapper.update(d_ShopDevice);

        return CustomResult.success("解绑成功");

    }
}
