package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.BookerService;
import com.caterbao.lumos.api.merch.service.MqttClientService;
import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.common.vo.FieldVo;
import com.caterbao.lumos.locals.dal.DeviceVoUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.BookerSlotMapper;
import com.caterbao.lumos.locals.dal.mapper.BookerStockMapper;
import com.caterbao.lumos.locals.dal.mapper.MerchDeviceMapper;
import com.caterbao.lumos.locals.dal.pojo.BookerSlot;
import com.caterbao.lumos.locals.dal.pojo.BookerStock;
import com.caterbao.lumos.locals.dal.pojo.MerchDevice;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookerServiceImpl implements BookerService {

    private MerchDeviceMapper merchDeviceMapper;
    private BookerSlotMapper bookerSlotMapper;
    private BookerStockMapper bookerStockMapper;
    private CacheFactory cacheFactory;
    private RedisTemplate redisTemplate;
    private MqttClientService mqttClientService;

    @Autowired(required = false)
    public void setMerchDeviceMapper(MerchDeviceMapper merchDeviceMapper) {
        this.merchDeviceMapper = merchDeviceMapper;
    }

    @Autowired(required = false)
    public void setBookerSlotMapper(BookerSlotMapper bookerSlotMapper) {
        this.bookerSlotMapper = bookerSlotMapper;
    }
    @Autowired(required = false)
    public void setBookerStockMapper(BookerStockMapper bookerStockMapper) {
        this.bookerStockMapper = bookerStockMapper;
    }

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired(required = false)
    public void setMqttClientService(MqttClientService mqttClientService) {
        this.mqttClientService = mqttClientService;
    }

    @Autowired(required = false)
    public void setCacheFactory(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    @Override
    public CustomResult<Object> init_list(String operater, String merchId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective = new LumosSelective();
        selective.addWhere("MerchId", merchId);


        long deviceCount = merchDeviceMapper.count(selective);

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
    public CustomResult<Object> list(String operater, String merchId, RopBookerList rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective=new LumosSelective();
        selective.addWhere("MerchId",merchId);
        selective.addWhere("SceneMode","2");
        selective.addWhere("DeviceCode",rop.getDeviceCode());
        List<MerchDeviceVw> d_Devices= merchDeviceMapper.find(selective);

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
    public CustomResult<Object> init_baseinfo(String operater, String merchId,String deviceId) {

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
    public CustomResult<Object> init_stock(String operater, String merchId,String deviceId) {

        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("DeviceId", deviceId);

        List<BookerSlot> d_BookerSlots = bookerSlotMapper.find(selective);

        List<Object> m_Slots = new ArrayList<>();

        for (BookerSlot d_BookerSlot : d_BookerSlots) {
            HashMap<String, Object> m_Slot = new HashMap<>();
            m_Slot.put("id", d_BookerSlot.getId());
            m_Slot.put("name", d_BookerSlot.getName());
            m_Slots.add(m_Slot);
        }

        HashMap<String, Object> ret = new HashMap<>();

        ret.put("slots", m_Slots);

        return result.success("初始成功",ret);

    }

    @Override
    public CustomResult<Object> stock(String operater, String merchId, RopBookerStock rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize,"CreateTime DESC");

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("DeviceId",rop.getDeviceId());

        if(!CommonUtil.isEmpty(rop.getSlotId())){
            selective.addWhere("SlotId",rop.getSlotId());
        }

        List<BookerStock> d_BookerStocks= bookerStockMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (BookerStock d_BookerStock :
                d_BookerStocks) {

            HashMap<String,Object> item=new HashMap<>();

            SkuInfo r_SkuInfo = cacheFactory.getProduct().getSkuInfo(d_BookerStock.getMerchId(), d_BookerStock.getSkuId());
            if (r_SkuInfo != null) {
                item.put("slotId", d_BookerStock.getSlotId());
                item.put("skuId", r_SkuInfo.getId());
                item.put("skuRfId", d_BookerStock.getSkuRfId());
                item.put("skuName", r_SkuInfo.getName());
                item.put("skuImgUrl", r_SkuInfo.getImgUrl());
                item.put("skuCumCode", r_SkuInfo.getCumCode());
                items.add(item);
            }
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
    public CustomResult<Object> edit(String operater, String merchId, RopBookerEdit rop) {

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

    @Override
    public CustomResult<Object>  rebootSys(String operater, String merchId, RopBookerRebootSys rop){
        return  sendCommand(merchId,rop.getId(),"reboot_sys",null);
    }

    public CustomResult<Object>  shutdownSys(String operater, String merchId, RopBookerShutdownSys rop){
        return  sendCommand(merchId,rop.getId(),"shutdown_sys",null);
    }

    public CustomResult<Object>  updateApp(String operater, String merchId, RopBookerUpdateApp rop){
        return  sendCommand(merchId,rop.getId(),"update_app",null);
    }

    public  CustomResult<Object> sendCommand(String merchId,String deviceId,String method,Object pms) {

        CustomResult<Object> result = new CustomResult<Object>();

        String topic="/a1A2Mq6w51n/" + deviceId + "/user/get";

        String msgId = IdWork.buildGuId();

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("id", msgId);
        payload.put("method", method);
        if(pms!=null) {
            payload.put("params", pms);
        }

        String str_Payload = JsonUtil.getJson(payload);

        mqttClientService.publish(topic,str_Payload, 1, false);

        redisTemplate.opsForValue().set("mqtt_msg:" + msgId, "0", 15, TimeUnit.SECONDS);


        long nDoMaxTime = 3 * 1000;
        long nDoStartTime = System.currentTimeMillis();
        long nDoLastTime = System.currentTimeMillis() - nDoStartTime;

        boolean isSendSuccess=false;

        while (nDoLastTime < nDoMaxTime) {

            Object msg = redisTemplate.opsForValue().get("mqtt_msg:" + msgId);
            if (msg != null) {
                if (msg.toString().equals("1")) {
                    isSendSuccess = true;
                    break;
                }
            }

            nDoLastTime = System.currentTimeMillis() - nDoStartTime;

        }

        if(isSendSuccess) {
            HashMap<String, Object> ret = new HashMap<>();
            ret.put("msgId", msgId);
            return result.success("发送成功", ret);
        }
        else
        {
            return result.fail("失败");
        }
    }

}
