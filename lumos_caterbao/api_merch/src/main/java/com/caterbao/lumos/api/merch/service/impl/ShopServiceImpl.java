package com.caterbao.lumos.api.merch.service.impl;


import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.ShopService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.common.vo.FileVo;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.ShopDeviceMapper;
import com.caterbao.lumos.locals.dal.mapper.ShopMapper;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import com.caterbao.lumos.locals.dal.pojo.ShopDevice;
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

    private ShopMapper shopMapper;
    private ShopDeviceMapper shopDeviceMapper;

    @Autowired(required = false)
    public void setShopMapper(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    @Autowired(required = false)
    public void setShopDeviceMapper(ShopDeviceMapper shopDeviceMapper) {
        this.shopDeviceMapper = shopDeviceMapper;
    }

    @Override
    public CustomResult<Object> list(String operater, String merchId, RopShopList rop) {
        CustomResult<Object> result = new CustomResult<>();
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
            item.put("imgUrl", FileVo.getFirstFileUrl(d_Shop.getDisplayImgUrls()));
            items.add(item);
        }

        long total = page.getTotal();
        PageResult<Object> ret = new PageResult<>();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return result.success("",ret);

    }

    @Override
    public CustomResult<Object> details(String operater, String merchId, String shopId) {
        CustomResult<Object> result = new CustomResult<>();
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

        return result.success("",ret);

    }

    @Override
    public CustomResult<Object> add(String operater, String merchId, RopShopAdd rop) {
        CustomResult<Object> result = new CustomResult<>();
        if(CommonUtil.isEmpty(merchId))
            return result.fail("????????????????????????");

        if(CommonUtil.isEmpty(rop.getName()))
            return result.fail("??????????????????");

        Shop d_Shop = new Shop();
        d_Shop.setMerchId(merchId);
        d_Shop.setName(rop.getName());

        if(shopMapper.isExistName(null,merchId,rop.getName())>0)
            return result.fail("??????????????????");

        d_Shop.setId(IdWork.buildGuId());
        d_Shop.setAddress(rop.getAddress());
        d_Shop.setContactName(rop.getContactName());
        d_Shop.setContactPhone(rop.getContactPhone());
        d_Shop.setContactAddress(rop.getContactAddress());
        d_Shop.setDisplayImgUrls(JsonUtil.getJson(rop.getDisplayImgUrls()));
        d_Shop.setCreator(operater);
        d_Shop.setCreateTime(CommonUtil.getDateTimeNow());

        long r_Shop_Insert = shopMapper.insert(d_Shop);

        if(r_Shop_Insert>0)
            return result.success("????????????");

        return result.success("????????????");
    }

    @Override
    public CustomResult init_edit(String operater, String merchId, String shopId) {
        CustomResult<Object> result = new CustomResult<>();
        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,Address,DisplayImgUrls,ContactName,ContactPhone,ContactAddress");
        selective.addWhere("ShopId",shopId);
        selective.addWhere("MerchId",merchId);

        Shop d_Shop = shopMapper.findOne(selective);

        if (d_Shop == null)
           return result.fail("????????????");
        
        HashMap<String,Object> ret=new HashMap<>();

        ret.put("id",d_Shop.getId());
        ret.put("name",d_Shop.getName());
        ret.put("address",d_Shop.getAddress());
        ret.put("displayImgUrls",JsonUtil.toObject(d_Shop.getDisplayImgUrls()));
        ret.put("contactName",d_Shop.getContactName());
        ret.put("contactPhone",d_Shop.getContactPhone());
        ret.put("contactAddress",d_Shop.getContactAddress());

        return result.success("????????????",ret);
    }

    @Override
    public CustomResult<Object> edit(String operater, String merchId, RopShopEdit rop) {
        CustomResult<Object> result = new CustomResult<>();
        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("ShopId",rop.getId());
        selective.addWhere("MerchId",merchId);

        Shop d_Shop = shopMapper.findOne(selective);

        if (d_Shop == null)
            return result.fail("???????????????");


        d_Shop.setName(rop.getName());
        d_Shop.setMerchId(merchId);

        if(shopMapper.isExistName(d_Shop.getId(),merchId,rop.getName())>0)
            return result.fail("??????????????????");


        d_Shop.setAddress(rop.getAddress());
        d_Shop.setDisplayImgUrls(JsonUtil.getJson(rop.getDisplayImgUrls()));
        d_Shop.setContactName(rop.getContactName());
        d_Shop.setContactPhone(rop.getContactPhone());
        d_Shop.setContactAddress(rop.getContactAddress());
        d_Shop.setMender(operater);
        d_Shop.setMendTime(CommonUtil.getDateTimeNow());

        long r_Shop_Update = shopMapper.update(d_Shop);
        if (r_Shop_Update>0)
            return result.success("????????????");

        return result.success("????????????");
    }

    @Override
    public CustomResult<Object> devices(String operater, String merchId, RopShopDevices rop) {
        CustomResult<Object> result = new CustomResult<>();
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

        return result.success("",ret);

    }

    @Override
    public CustomResult<Object> unDevices(String operater, String merchId, RopShopDevices rop) {
        CustomResult<Object> result = new CustomResult<>();
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

        return result.success("",ret);

    }

    @Override
    @Transactional
    public CustomResult<Object> bindDevice(String operater, String merchId, RopShopBindDevice rop) {
        CustomResult<Object> result = new CustomResult<>();
        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("StoreId",rop.getStoreId());
        selective.addWhere("ShopId",rop.getShopId());
        selective.addWhere("DeviceId",rop.getDeviceId());

        ShopDevice d_ShopDevice=shopDeviceMapper.findOne(selective);

        if(d_ShopDevice==null) {
            d_ShopDevice = new ShopDevice();
            d_ShopDevice.setId(IdWork.buildGuId());
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

        return result.success("????????????");

    }

    @Override
    @Transactional
    public CustomResult<Object> unBindDevice(String operater, String merchId, RopShopBindDevice rop) {
        CustomResult<Object> result = new CustomResult<>();
        ShopDevice d_ShopDevice = new ShopDevice();
        d_ShopDevice.setMerchId(merchId);
        d_ShopDevice.setStoreId(rop.getStoreId());
        d_ShopDevice.setShopId(rop.getShopId());
        d_ShopDevice.setDeviceId(rop.getDeviceId());
        d_ShopDevice.setBindStatus(2);
        d_ShopDevice.setMender(operater);
        d_ShopDevice.setMendTime(CommonUtil.getDateTimeNow());
        shopDeviceMapper.update(d_ShopDevice);

        return result.success("????????????");

    }
}
