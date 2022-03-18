package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopDeviceBookers;
import com.caterbao.lumos.api.merch.rop.RopDeviceEdit;
import com.caterbao.lumos.api.merch.rop.vo.FieldVo;
import com.caterbao.lumos.api.merch.service.DeviceService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.DeviceVoUtil;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.MerchDeviceMapper;
import com.caterbao.lumos.locals.dal.pojo.MerchDevice;
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

    private MerchDeviceMapper merchDeviceMapper;

    @Autowired(required = false)
    public void setMerchDeviceMapper(MerchDeviceMapper merchDeviceMapper) {
        this.merchDeviceMapper = merchDeviceMapper;
    }

    @Override
    public CustomResult<Object> init_bookers(String operater, String merchId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective_DeviceCount = new LumosSelective();
        selective_DeviceCount.addWhere("MerchId", merchId);


        long deviceCount = merchDeviceMapper.count(selective_DeviceCount);

        HashMap<String, Object> ret = new HashMap<>();

        ret.put("deviceCount", deviceCount);


        return result.success("初始成功", ret);
    }

    public FieldVo getStatus(){
        FieldVo model=new FieldVo();

        return model;
    }

    public String getBelongName(String storeName,String shopName) {
        if (CommonUtil.isEmpty(shopName))
            return "未绑定";
        return storeName + "/" + shopName;
    }

    @Override
    public CustomResult<Object> bookers(String operater, String merchId, RopDeviceBookers rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective_MerchDevice=new LumosSelective();
        selective_MerchDevice.addWhere("MerchId",merchId);
        selective_MerchDevice.addWhere("SceneMode","2");
        selective_MerchDevice.addWhere("DeviceCode",rop.getDeviceCode());
        List<MerchDeviceVw> d_Devices= merchDeviceMapper.find(selective_MerchDevice);

        List<Object> items=new ArrayList<>();

        for (MerchDeviceVw d_Device: d_Devices ) {

            HashMap<String, Object> item = new HashMap<>();

            item.put("id", d_Device.getId());
            item.put("cumCode", d_Device.getCumCode());
            item.put("code", DeviceVoUtil.getCode(d_Device.getId(),d_Device.getCumCode()));
            item.put("imgUrl", d_Device.getImgUrl());
            item.put("status",getStatus());
            item.put("belongName",getBelongName(d_Device.getStoreName(),d_Device.getShopName()));
            item.put("lastRunTime",CommonUtil.toDateTimeStr(d_Device.getLastRunTime()));
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
    public CustomResult<Object> init_manage(String operater, String merchId,String deviceId) {

        CustomResult<Object> result = new CustomResult<>();

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

        return result.success("初始成功",ret);

    }

    @Override
    public CustomResult<Object> init_manage_baseinfo(String operater, String merchId,String deviceId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective=new LumosSelective();
        selective.addWhere("MerchId",merchId);
        selective.addWhere("DeviceId",deviceId);
        MerchDeviceVw d_Device = merchDeviceMapper.findOne(selective);


        HashMap<String,Object> ret=new HashMap<>();

        ret.put("id",d_Device.getId());
        ret.put("cumCode",d_Device.getCumCode());
        ret.put("appVerName",d_Device.getAppVerName());
        ret.put("sysVerName",d_Device.getSysVerName());
        ret.put("ctrlVerName",d_Device.getCtrlVerName());
        ret.put("belongName",getBelongName(d_Device.getStoreName(),d_Device.getShopName()));
        return result.success("初始成功",ret);
    }

    @Override
    public CustomResult<Object> edit(String operater, String merchId, RopDeviceEdit rop) {

        CustomResult<Object> result = new CustomResult<>();

        if(!CommonUtil.isEmpty(rop.getCumCode())) {
            if (merchDeviceMapper.isExistCumCode(rop.getId(), merchId, rop.getCumCode()) > 0) {
                return result.fail("自编码已经存在");
            }
        }

        MerchDevice d_MerchDevice = new MerchDevice();
        d_MerchDevice.setMerchId(merchId);
        d_MerchDevice.setDeviceId(rop.getId());
        d_MerchDevice.setCumCode(rop.getCumCode());
        d_MerchDevice.setCreator(operater);
        d_MerchDevice.setMendTime(CommonUtil.getDateTimeNow());
        long r_MerchDevice_Update = merchDeviceMapper.update(d_MerchDevice);

        if (r_MerchDevice_Update > 0) {
            return result.success("保存成功");
        }

        return result.fail("保存失败");
    }
}
