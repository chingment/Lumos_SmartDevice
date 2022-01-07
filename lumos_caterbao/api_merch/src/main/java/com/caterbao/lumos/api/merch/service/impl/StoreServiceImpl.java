package com.caterbao.lumos.api.merch.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.caterbao.lumos.api.merch.rop.RopStoreBindShop;
import com.caterbao.lumos.api.merch.rop.RopStoreEdit;
import com.caterbao.lumos.api.merch.rop.RopStoreList;
import com.caterbao.lumos.api.merch.rop.RopStoreShops;
import com.caterbao.lumos.api.merch.service.StoreService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.mapper.ShopMapper;
import com.caterbao.lumos.locals.dal.mapper.StoreMapper;
import com.caterbao.lumos.locals.dal.mapper.StoreShopMapper;
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

    @Override
    public CustomResult list(String operater, String merchId, RopStoreList rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page =PageHelper.startPage(pageNum, pageSize);

        HashMap<String,String> selective=new HashMap<>();
        selective.put("fields","id,name,displayImgUrls");
        selective.put("where_name",rop.getStoreName());
        selective.put("where_merchId",merchId);
        List<Store> d_Stores = storeMapper.list(selective);

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

        Page<?> page = PageHelper.startPage(1, 1024);

        HashMap<String, String> selective = new HashMap<>();
        selective.put("fields", "id,name,displayImgUrls");
        selective.put("where_merchId", merchId);
        List<Store> d_Stores = storeMapper.list(selective);

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

        Store d_Store = storeMapper.findByStoreId(storeId);

        if(d_Store==null)
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
    public CustomResult edit(String operater, String merchId, RopStoreEdit rop) {

        Store d_Store = storeMapper.findByStoreId(rop.getId());

        if (d_Store == null)
            return CustomResult.fail("数据不存在");


        d_Store.setName(rop.getName());
        d_Store.setMerchId(merchId);

        //if(storeMapper.isExistName(d_Store)>0)
        //    return CustomResult.fail("名称已经存在");

        d_Store.setDisplayImgUrls(JsonUtil.getJson(rop.getDisplayImgUrls()));

        d_Store.setContactName(rop.getContactName());
        d_Store.setContactPhone(rop.getContactPhone());
        d_Store.setContactAddress(rop.getContactAddress());
        d_Store.setMender(operater);
        d_Store.setMendTime(CommonUtil.getDateTimeNow());

        long isflag = storeMapper.update(d_Store);
        if (isflag>0)
            return CustomResult.success("保存成功");

        return CustomResult.success("保存失败");
    }

    @Override
    public CustomResult shops(String operater, String merchId, RopStoreShops rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        Page<?> page =PageHelper.startPage(pageNum, pageSize);

        HashMap<String,String> selective=new HashMap<>();
        selective.put("fields","id,name,displayImgUrls");
        selective.put("where_shopName",rop.getShopName());
        selective.put("where_merchId",merchId);
        selective.put("where_storeId",rop.getStoreId());

        List<Shop> d_Shops = storeShopMapper.getBindShops(selective);

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
    public CustomResult unShops(String operater, String merchId, RopStoreShops rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        Page<?> page =PageHelper.startPage(pageNum, pageSize);

        HashMap<String,String> selective=new HashMap<>();
        selective.put("fields","id,name,displayImgUrls");
        selective.put("where_shopName",rop.getShopName());
        selective.put("where_merchId",merchId);
        selective.put("where_storeId",rop.getStoreId());

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



        StoreShop d_StoreShop = new StoreShop();
        d_StoreShop.setId(IdWork.generateGUID());
        d_StoreShop.setMerchId(merchId);
        d_StoreShop.setStoreId(rop.getStoreId());
        d_StoreShop.setShopId(rop.getShopId());
        d_StoreShop.setCreator(operater);
        d_StoreShop.setCreateTime(CommonUtil.getDateTimeNow());

        storeShopMapper.insert(d_StoreShop);

        return CustomResult.success("绑定成功");

    }

    @Override
    public CustomResult unBindShop(String operater, String merchId, RopStoreBindShop rop) {

        StoreShop d_StoreShop = new StoreShop();
        d_StoreShop.setMerchId(merchId);
        d_StoreShop.setStoreId(rop.getStoreId());
        d_StoreShop.setShopId(rop.getShopId());
        storeShopMapper.delete(d_StoreShop);

        return CustomResult.success("解绑成功");
    }
}
