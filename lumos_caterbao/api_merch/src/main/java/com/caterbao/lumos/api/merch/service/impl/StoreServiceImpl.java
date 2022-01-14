package com.caterbao.lumos.api.merch.service.impl;


import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.StoreService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.*;
import com.caterbao.lumos.locals.dal.pojo.Shop;
import com.caterbao.lumos.locals.dal.pojo.Store;
import com.caterbao.lumos.locals.dal.pojo.StoreShop;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StoreShopMapper storeShopMapper;

    @Autowired
    private ShopDeviceMapper shopDeviceMapper;

    @Override
    public CustomResult list(String operater, String merchId, RopStoreList rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page =PageHelper.startPage(pageNum, pageSize);


        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,DisplayImgUrls");
        selective.addWhere("Name",rop.getStoreName());
        selective.addWhere("MerchId",merchId);

        List<Store> d_Stores = storeMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (Store d_Store:
                d_Stores ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_Store.getId());
            item.put("name",d_Store.getName());
            item.put("imgUrl", ImgVo.getMainImgUrl(d_Store.getDisplayImgUrls()));
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
    public CustomResult init_manage(String operater, String merchId, String storeId) {

        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name");
        selective.addWhere("MerchId",merchId);

        List<Store> d_Stores = storeMapper.find(selective);
        List<Object> m_Stores = new ArrayList<>();

        for (Store d_Store :
                d_Stores) {
            HashMap<String, Object> m_Store = new HashMap<>();
            m_Store.put("id", d_Store.getId());
            m_Store.put("name", d_Store.getName());
            m_Stores.add(m_Store);
        }

        HashMap<String, Object> ret = new HashMap<>();

        ret.put("stores", m_Stores);

        return CustomResult.success("初始成功",ret);
    }

    @Override
    public CustomResult init_manage_baseinfo(String operater, String merchId, String storeId) {

        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,DisplayImgUrls,ContactName,ContactPhone,ContactAddress");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("StoreId",storeId);
        Store d_Store = storeMapper.findOne(selective);

        if(d_Store == null)
            return CustomResult.fail("初始失败");

        HashMap<String,Object> ret=new HashMap<>();

        ret.put("id",d_Store.getId());
        ret.put("name",d_Store.getName());
        ret.put("displayImgUrls",JsonUtil.toObject(d_Store.getDisplayImgUrls()));
        ret.put("contactName",d_Store.getContactName());
        ret.put("contactPhone",d_Store.getContactPhone());
        ret.put("contactAddress",d_Store.getContactAddress());

        return CustomResult.success("初始成功",ret);

    }

    @Override
    public CustomResult shops(String operater, String merchId, RopStoreShops rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        Page<?> page =PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,DisplayImgUrls");
        selective.addWhere("ShopName",rop.getShopName());
        selective.addWhere("MerchId",merchId);
        selective.addWhere("StoreId",rop.getStoreId());


        List<Shop> d_Shops = storeShopMapper.getBindShops(selective);

        List<Object> items=new ArrayList<>();

        for (Shop d_Shop:
                d_Shops ) {

            HashMap<String,Object> item=new HashMap<>();

            LumosSelective selective_ShopDevice=new LumosSelective();
            selective_ShopDevice.addWhere("MerchId",merchId);
            selective_ShopDevice.addWhere("StoreId",rop.getStoreId());
            selective_ShopDevice.addWhere("ShopId",d_Shop.getId());
            selective_ShopDevice.addWhere("BindStatus","1");//已绑定的设备

            long deviceCount=shopDeviceMapper.count(selective_ShopDevice);

            item.put("id",d_Shop.getId());
            item.put("name",d_Shop.getName());
            item.put("imgUrl", ImgVo.getMainImgUrl(d_Shop.getDisplayImgUrls()));
            item.put("deviceCount",deviceCount);
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
    public CustomResult unShops(String operater, String merchId, RopStoreShops rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        Page<?> page =PageHelper.startPage(pageNum, pageSize);


        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,DisplayImgUrls");
        selective.addWhere("ShopName",rop.getShopName());
        selective.addWhere("MerchId",merchId);
        selective.addWhere("StoreId",rop.getStoreId());


        List<Shop> d_Shops = storeShopMapper.getUnBindShops(selective);

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
    public CustomResult bindShop(String operater, String merchId, RopStoreBindShop rop) {

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("StoreId",rop.getStoreId());
        selective.addWhere("ShopId",rop.getShopId());

        StoreShop d_StoreShop=storeShopMapper.findOne(selective);

        if(d_StoreShop==null) {
            d_StoreShop = new StoreShop();
            d_StoreShop.setId(IdWork.generateGUID());
            d_StoreShop.setMerchId(merchId);
            d_StoreShop.setStoreId(rop.getStoreId());
            d_StoreShop.setShopId(rop.getShopId());
            d_StoreShop.setBindStatus(1);
            d_StoreShop.setCreator(operater);
            d_StoreShop.setCreateTime(CommonUtil.getDateTimeNow());
            storeShopMapper.insert(d_StoreShop);
        }
        else {
            d_StoreShop.setBindStatus(1);
            d_StoreShop.setMender(operater);
            d_StoreShop.setMendTime(CommonUtil.getDateTimeNow());
            storeShopMapper.update(d_StoreShop);
        }

        return CustomResult.success("绑定成功");

    }

    @Override
    public CustomResult unBindShop(String operater, String merchId, RopStoreBindShop rop) {

        StoreShop d_StoreShop = new StoreShop();
        d_StoreShop.setMerchId(merchId);
        d_StoreShop.setStoreId(rop.getStoreId());
        d_StoreShop.setShopId(rop.getShopId());
        d_StoreShop.setBindStatus(2);
        d_StoreShop.setMender(operater);
        d_StoreShop.setMendTime(CommonUtil.getDateTimeNow());
        storeShopMapper.update(d_StoreShop);

        return CustomResult.success("解绑成功");
    }

}
