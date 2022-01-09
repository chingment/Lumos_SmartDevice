package com.caterbao.lumos.api.device.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.caterbao.lumos.api.device.rop.RetDeviceInitData;
import com.caterbao.lumos.api.device.rop.RopDeviceInitData;
import com.caterbao.lumos.api.device.rop.model.CabinetBean;
import com.caterbao.lumos.api.device.rop.model.DeviceBean;
import com.caterbao.lumos.api.device.service.DeviceService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.dal.mapper.DeviceCabinetMapper;
import com.caterbao.lumos.locals.dal.mapper.DeviceMapper;
import com.caterbao.lumos.locals.dal.pojo.Device;
import com.caterbao.lumos.locals.dal.pojo.DeviceCabinet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceCabinetMapper deviceCabinetMapper;

    @Override
    public CustomResult init(String operater, String merchId, RopDeviceInitData rop) {

        if (rop == null)
            return CustomResult.fail("初始化数据对象为空");

        if (StringUtils.isEmpty(rop.getDeviceId()))
            return CustomResult.fail("设备编码为空");

        HashMap<String, String> d_Device_Selective = new HashMap<>();
        d_Device_Selective.put("fields", "*");
        d_Device_Selective.put("where_DeviceId", rop.getDeviceId());

        Device d_Device = deviceMapper.findByDeviceId(d_Device_Selective);

        if (d_Device == null)
            return CustomResult.fail("设备编码未注册");

        if (StringUtils.isEmpty(d_Device.getCurMerchId()))
            return CustomResult.fail("设备未绑定商户");

        if (StringUtils.isEmpty(d_Device.getCurStoreId()))
            return CustomResult.fail("设备未绑定店铺");

        if (StringUtils.isEmpty(d_Device.getCurShopId()))
            return CustomResult.fail("设备未绑定门店");


        d_Device.setAppVerCode(rop.getAppVerCode());
        d_Device.setAppVerName(rop.getAppVerName());
        d_Device.setSysVerName(rop.getSysVerName());
        d_Device.setCtrlVerName(rop.getCtrlVerName());
        d_Device.setMendTime(CommonUtil.getDateTimeNow());

        deviceMapper.update(d_Device);

        List<DeviceCabinet> d_Cabinets = deviceCabinetMapper.findByDeviceId(rop.getDeviceId());

        RetDeviceInitData ret = new RetDeviceInitData();

        DeviceBean m_Device = new DeviceBean();
        m_Device.setDeviceId(d_Device.getId());
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

        ret.setCustomData(customData);

        return CustomResult.success("获取成功", ret);
    }
}
