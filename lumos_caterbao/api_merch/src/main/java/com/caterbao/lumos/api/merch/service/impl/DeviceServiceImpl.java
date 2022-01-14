package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopDeviceBookers;
import com.caterbao.lumos.api.merch.service.DeviceService;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.ImgVo;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.common.PageResult;
import com.caterbao.lumos.locals.dal.DeviceVoUtil;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.MerchDeviceMapper;
import com.caterbao.lumos.locals.dal.pojo.Store;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private MerchDeviceMapper merchDeviceMapper;

    @Override
    public CustomResult init_bookers(String operater, String merchId) {


        LumosSelective selective_DeviceCount=new LumosSelective();
        selective_DeviceCount.addWhere("MerchId",merchId);


        long deviceCount=merchDeviceMapper.count(selective_DeviceCount);

        HashMap<String,Object> ret=new HashMap<>();

        ret.put("deviceCount",deviceCount);


        return CustomResult.success("初始成功",ret);
    }

    @Override
    public CustomResult bookers(String operater, String merchId, RopDeviceBookers rop) {

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective_MerchDevice=new LumosSelective();
        selective_MerchDevice.addWhere("MerchId",merchId);
        selective_MerchDevice.addWhere("SceneMode","2");
        selective_MerchDevice.addWhere("DeviceCode",rop.getDeviceCode());
        List<MerchDeviceVw> d_MerchDevices= merchDeviceMapper.find(selective_MerchDevice);

        List<Object> items=new ArrayList<>();

        for (MerchDeviceVw d_MerchDevice: d_MerchDevices ) {

            HashMap<String, Object> item = new HashMap<>();

            item.put("id", d_MerchDevice.getId());
            item.put("cumCode", d_MerchDevice.getCumCode());
            item.put("code", DeviceVoUtil.getCode(d_MerchDevice.getId(),d_MerchDevice.getCumCode()));
            item.put("imgUrl", d_MerchDevice.getImgUrl());

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
    public CustomResult init_manage(String operater, String merchId,String deviceId) {


        LumosSelective selective=new LumosSelective();
        selective.addWhere("MerchId",merchId);

        List<MerchDeviceVw> d_Devices = merchDeviceMapper.find(selective);
        List<Object> m_Devices = new ArrayList<>();

        for (MerchDeviceVw d_Device :
                d_Devices) {
            HashMap<String, Object> m_Device = new HashMap<>();
            m_Device.put("id", d_Device.getId());
            m_Device.put("name", DeviceVoUtil.getCode(d_Device.getId(),d_Device.getCumCode()));
            m_Devices.add(m_Device);
        }

        HashMap<String, Object> ret = new HashMap<>();

        ret.put("devices", m_Devices);

        return CustomResult.success("初始成功",ret);

    }

    @Override
    public CustomResult init_manage_baseinfo(String operater, String merchId,String deviceId) {

        LumosSelective selective=new LumosSelective();
        selective.addWhere("MerchId",merchId);
        selective.addWhere("Device",merchId);
        MerchDeviceVw d_Device = merchDeviceMapper.findOne(selective);


        HashMap<String,Object> ret=new HashMap<>();


        return CustomResult.success("初始成功",ret);
    }
}
