package com.caterbao.lumos.api.device.service.impl;

import com.caterbao.lumos.api.device.rop.RetDeviceCheckAppVersion;
import com.caterbao.lumos.api.device.rop.RetDeviceInitData;
import com.caterbao.lumos.api.device.rop.RopDeviceCheckAppVerion;
import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.api.device.rop.vo.*;
import com.caterbao.lumos.api.device.service.DeviceService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.vo.FileVo;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.*;
import com.caterbao.lumos.locals.dal.pojo.*;
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
    private DeviceDriveMapper deviceDriveMapper;
    private BookerSlotMapper bookerSlotMapper;
    private MerchDeviceMapper merchDeviceMapper;
    private AdSpaceMapper adSpaceMapper;
    private AdCreativeMapper adCreativeMapper;
    private AppSoftMapper appSoftMapper;

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


    @Autowired(required = false)
    public void setDeviceDriveMapper(DeviceDriveMapper deviceDriveMapper) {
        this.deviceDriveMapper = deviceDriveMapper;
    }

    @Autowired(required = false)
    public void setBookerSlotMapper(BookerSlotMapper bookerSlotMapper) {
        this.bookerSlotMapper = bookerSlotMapper;
    }

    @Autowired(required = false)
    public void setAppSoftMapper(AppSoftMapper appSoftMapper) {
        this.appSoftMapper = appSoftMapper;
    }

    @Override
    public CustomResult<RetDeviceInitData> init(String operater, String merchId, RopDeviceInitData rop)  {


        CustomResult<RetDeviceInitData> result=new CustomResult<>();

        if (rop == null)
            return result.fail("初始化数据对象为空");

        if (CommonUtil.isEmpty(rop.getDeviceId()))
            return result.fail("设备编码为空");

        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,Name,SceneMode,VersionMode");
        selective.addWhere("DeviceId",rop.getDeviceId());

        Device d_Device = deviceMapper.findOne(selective);

        if (d_Device == null)
            return result.fail("设备编码未注册");

        selective=new LumosSelective();
        selective.addWhere("DeviceId",rop.getDeviceId());
        selective.addWhere("BindStatus","1");
        MerchDeviceVw d_MerchDevice = merchDeviceMapper.findOne(selective);

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

        RetDeviceInitData ret = new RetDeviceInitData();

        DeviceVo m_Device = new DeviceVo();
        m_Device.setDeviceId(d_Device.getId());
        m_Device.setMerchId(d_MerchDevice.getMerchId());
        m_Device.setName(d_Device.getName());
        m_Device.setSceneMode(d_Device.getSceneMode());
        m_Device.setVersionMode(d_Device.getVersionMode());

        selective=new LumosSelective();
        selective.setFields("DriveId,Name,ComId,ComPrl,ComBaud");
        selective.addWhere("DeviceId",rop.getDeviceId());

        List<DeviceDrive> d_Drives = deviceDriveMapper.find(selective);

        HashMap<String, DriveVo> m_Drives=new HashMap<>();
        if(d_Drives!=null) {
            for (DeviceDrive d_Drive : d_Drives) {
                DriveVo m_Drive = new DriveVo();
                m_Drive.setDriveId(d_Drive.getDriveId());
                m_Drive.setName(d_Drive.getName());
                m_Drive.setComId(d_Drive.getComId());
                m_Drive.setComPrl(d_Drive.getComPrl());
                m_Drive.setComBaud(d_Drive.getComBaud());
                m_Drives.put(d_Drive.getDriveId(), m_Drive);
            }
        }

        m_Device.setDrives(m_Drives);


        MqttVo m_Mqtt=new MqttVo();
        m_Mqtt.setHost("tcp://112.74.179.185:1883");
        m_Mqtt.setUserName("admin");
        m_Mqtt.setPassword("public");
        m_Mqtt.setClientId(rop.getDeviceId());
        m_Mqtt.setSubTopic("/a1A2Mq6w51n/" + rop.getDeviceId() + "/user/get");
        m_Mqtt.setPubTopic("/a1A2Mq6w51n/" + rop.getDeviceId() + "/user/update");
        m_Device.setMqtt(m_Mqtt);

        ret.setDevice(m_Device);

        HashMap<String, Object> customData = new HashMap<>();

        customData.put("ads",getAds(d_MerchDevice.getMerchId(),d_MerchDevice.getId()));
        customData.put("slots",getBookerSlots(d_MerchDevice.getMerchId(),d_MerchDevice.getId()));

        ret.setCustomData(customData);

        return result.success("获取成功", ret);
    }

    @Override
    public CustomResult<RetDeviceCheckAppVersion> checkAppVerion(String operater,RopDeviceCheckAppVerion rop) {

        CustomResult<RetDeviceCheckAppVersion> result = new CustomResult<>();

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("AppId", rop.getAppId());
        selective.addWhere("AppKey", rop.getAppKey());

        AppSoft d_AppSoft = appSoftMapper.findOne(selective);


        if (d_AppSoft == null)
            return result.fail("找不到应用");

        RetDeviceCheckAppVersion ret=new RetDeviceCheckAppVersion();
        ret.setDownloadUrl(d_AppSoft.getDownloadUrl());
        ret.setVersionCode(d_AppSoft.getVersionCode());
        ret.setVersionName(d_AppSoft.getVersionName());

        return result.success("",ret);
    }


    private HashMap<String, AdVo> getAds(String merchId, String deviceId) {
        HashMap<String, AdVo> ads = new HashMap<>();

        String[] spaceIds = new String[]{"101"};

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId", merchId);
        selective.addWhere("SpaceIds", spaceIds);

        List<AdSpace> d_AdSpaces = adSpaceMapper.find(selective);


        for (AdSpace d_AdSpace : d_AdSpaces) {
            AdVo ad = new AdVo();
            ad.setSpaceId(d_AdSpace.getId());
            ad.setName(d_AdSpace.getName());

            selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("MerchId", merchId);
            selective.addWhere("SpaceId", d_AdSpace.getId());
            selective.addWhere("Status", "1");
            selective.addWhere("StartTime", CommonUtil.toDateTimeStr(CommonUtil.getDateTimeNow()));
            selective.addWhere("EndTime", CommonUtil.toDateTimeStr(CommonUtil.getDateTimeNow()));
            List<AdCreative> d_AdCreatives = adCreativeMapper.find(selective);

            List<AdCreativeVo> m_AdCreatives = new ArrayList<>();
            for (AdCreative d_AdCreative : d_AdCreatives) {

                AdCreativeVo m_AdCreative = new AdCreativeVo();

                m_AdCreative.setFileUrl(FileVo.getFirstFileUrl(d_AdCreative.getFileUrl()));

                m_AdCreatives.add(m_AdCreative);
            }

            ad.setCreatives(m_AdCreatives);

            ads.put(d_AdSpace.getId(), ad);
        }

        return ads;
    }

    private List<BookerSlotVo> getBookerSlots(String merchId, String deviceId) {
        List<BookerSlotVo> m_Slots = new ArrayList<>();


        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("DeviceId",deviceId);
        List<BookerSlot> d_Slots = bookerSlotMapper.find(selective);

        if(d_Slots!=null) {
            for (BookerSlot d_Slot : d_Slots) {
                BookerSlotVo m_Slot = new BookerSlotVo();
                m_Slot.setSlotId(d_Slot.getSlotId());
                m_Slot.setName(d_Slot.getName());
                m_Slot.setLockeqId(d_Slot.getLockeqId());
                m_Slot.setLockeqAnt(d_Slot.getLockeqAnt());
                m_Slot.setRfeqId(d_Slot.getRfeqId());
                m_Slot.setRfeqAnt(d_Slot.getRfeqAnt());
                m_Slots.add(m_Slot);
            }
        }


        return m_Slots;
    }

}
