package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.RetDeviceInitData;
import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.api.device.rop.model.AdBean;
import com.caterbao.lumos.api.device.rop.model.AdCreativeBean;
import com.caterbao.lumos.api.device.rop.model.CabinetBean;
import com.caterbao.lumos.api.device.rop.model.DeviceBean;
import com.caterbao.lumos.api.device.service.DeviceService;
import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.*;
import com.caterbao.lumos.locals.dal.pojo.AdCreative;
import com.caterbao.lumos.locals.dal.pojo.AdSpace;
import com.caterbao.lumos.locals.dal.pojo.Device;
import com.caterbao.lumos.locals.dal.pojo.DeviceCabinet;
import com.caterbao.lumos.locals.dal.vw.MerchDeviceVw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService{

    private DeviceMapper deviceMapper;
    private DeviceCabinetMapper deviceCabinetMapper;
    private MerchDeviceMapper merchDeviceMapper;
    private AdSpaceMapper adSpaceMapper;
    private AdCreativeMapper adCreativeMapper;

    @Autowired(required = false)
    public void setDeviceMapper(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    @Autowired(required = false)
    public void setDeviceCabinetMapper(DeviceCabinetMapper deviceCabinetMapper) {
        this.deviceCabinetMapper = deviceCabinetMapper;
    }

    @Autowired(required = false)
    public void setMerchDeviceMapper(MerchDeviceMapper merchDeviceMapper) {
        this.merchDeviceMapper = merchDeviceMapper;
    }

    @Autowired(required = false)
    public void setAdCreativeMapper(AdCreativeMapper adCreativeMapper) {
        this.adCreativeMapper = adCreativeMapper;
    }

    @Autowired(required = false)
    public void setAdSpaceMapper(AdSpaceMapper adSpaceMapper) {
        this.adSpaceMapper = adSpaceMapper;
    }

    @Override
    public CustomResult<RetDeviceInitData> init(String operater, String merchId, RopDeviceInitData rop)  {


        CustomResult<RetDeviceInitData> result=new CustomResult<>();

        if (rop == null)
            return result.fail("初始化数据对象为空");

        if (CommonUtil.isEmpty(rop.getDeviceId()))
            return result.fail("设备编码为空");

        LumosSelective selective_Device=new LumosSelective();
        selective_Device.setFields("Id,Name,SceneMode,VersionMode");
        selective_Device.addWhere("DeviceId",rop.getDeviceId());

        Device d_Device = deviceMapper.findOne(selective_Device);

        if (d_Device == null)
            return result.fail("设备编码未注册");

        LumosSelective selective_MerchDevice=new LumosSelective();
        selective_MerchDevice.addWhere("DeviceId",rop.getDeviceId());
        selective_MerchDevice.addWhere("BindStatus","1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective_MerchDevice);

        if (d_MerchDevice==null)
            return result.fail("设备未绑定商户");

        if (CommonUtil.isEmpty(d_MerchDevice.getStoreId()))
            return result.fail("设备未绑定店铺");

        if (CommonUtil.isEmpty(d_MerchDevice.getShopId()))
            return result.fail("设备未绑定门店");

        d_Device.setAppVerCode(rop.getAppVerCode());
        d_Device.setAppVerName(rop.getAppVerName());
        d_Device.setSysVerName(rop.getSysVerName());
        d_Device.setCtrlVerName(rop.getCtrlVerName());
        d_Device.setMendTime(CommonUtil.getDateTimeNow());

        deviceMapper.update(d_Device);

        LumosSelective selective_Cabinet=new LumosSelective();
        selective_Cabinet.setFields("CabinetId,Name,ComId,ComPrl,ComBaud,Layout,Priority");
        selective_Cabinet.addWhere("DeviceId",rop.getDeviceId());

        List<DeviceCabinet> d_Cabinets = deviceCabinetMapper.find(selective_Cabinet);

        RetDeviceInitData ret = new RetDeviceInitData();

        DeviceBean m_Device = new DeviceBean();
        m_Device.setDeviceId(d_Device.getId());
        m_Device.setMerchId(d_MerchDevice.getMerchId());
        m_Device.setName(d_Device.getName());
        m_Device.setSceneMode(d_Device.getSceneMode());
        m_Device.setVersionMode(d_Device.getVersionMode());

        HashMap<String, CabinetBean> m_Cabinets=new HashMap<>();
        if(d_Cabinets!=null) {
            for (DeviceCabinet d_Cabinet : d_Cabinets) {
                if(!m_Cabinets.containsKey(d_Cabinet.getCabinetId())) {
                    CabinetBean m_Cabinet = new CabinetBean();
                    m_Cabinet.setCabinetId(d_Cabinet.getCabinetId());
                    m_Cabinet.setName(d_Cabinet.getName());
                    m_Cabinet.setComId(d_Cabinet.getComId());
                    m_Cabinet.setComPrl(d_Cabinet.getComPrl());
                    m_Cabinet.setComBaud(d_Cabinet.getComBaud());
                    m_Cabinet.setLayout(d_Cabinet.getLayout());
                    m_Cabinet.setPriority(d_Cabinet.getPriority());
                    m_Cabinets.put(d_Cabinet.getCabinetId(), m_Cabinet);
                }
            }
        }

        m_Device.setCabinets(m_Cabinets);

        ret.setDevice(m_Device);

        HashMap<String, Object> customData = new HashMap<>();

        customData.put("ads",getAds(d_MerchDevice.getMerchId(),d_MerchDevice.getId()));

        ret.setCustomData(customData);

        return result.success("获取成功", ret);
    }



    private HashMap<String, AdBean> getAds(String merchId,String deviceId) {
        HashMap<String, AdBean> ads = new HashMap<>();

        String[] spaceIds = new String[]{"101"};

        LumosSelective selective_AdSpace = new LumosSelective();
        selective_AdSpace.setFields("*");
        selective_AdSpace.addWhere("MerchId", merchId);
        selective_AdSpace.addWhere("SpaceIds", spaceIds);

        List<AdSpace> d_AdSpaces = adSpaceMapper.find(selective_AdSpace);


        for (AdSpace d_AdSpace : d_AdSpaces) {
            AdBean ad = new AdBean();
            ad.setSpaceId(d_AdSpace.getId());
            ad.setName(d_AdSpace.getName());

            LumosSelective selective_AdCreative = new LumosSelective();
            selective_AdCreative.setFields("*");
            selective_AdCreative.addWhere("MerchId", merchId);
            selective_AdCreative.addWhere("SpaceId", d_AdSpace.getId());
            selective_AdCreative.addWhere("Status", "1");
            selective_AdCreative.addWhere("StartTime", CommonUtil.toDateTimeStr(CommonUtil.getDateTimeNow()));
            selective_AdCreative.addWhere("EndTime", CommonUtil.toDateTimeStr(CommonUtil.getDateTimeNow()));
            List<AdCreative> d_AdCreatives = adCreativeMapper.find(selective_AdCreative);

            List<AdCreativeBean> m_AdCreatives = new ArrayList<>();
            for (AdCreative d_AdCreative : d_AdCreatives) {

                AdCreativeBean m_AdCreative = new AdCreativeBean();

                m_AdCreative.setFileUrl("http://file.17fanju.com/upload/test.mp4");

                m_AdCreatives.add(m_AdCreative);
            }

            ad.setCreatives(m_AdCreatives);

            ads.put(d_AdSpace.getId(), ad);
        }

        return ads;
    }

}
