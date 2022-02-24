package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopAdCreativeAdd;
import com.caterbao.lumos.api.merch.rop.RopAdCreativeEdit;
import com.caterbao.lumos.api.merch.rop.RopAdCreatives;
import com.caterbao.lumos.api.merch.rop.RopAdSpaces;
import com.caterbao.lumos.api.merch.service.AdService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.PageResult;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.AdCreativeMapper;
import com.caterbao.lumos.locals.dal.mapper.AdSpaceMapper;
import com.caterbao.lumos.locals.dal.pojo.AdCreative;
import com.caterbao.lumos.locals.dal.pojo.AdSpace;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    private AdSpaceMapper adSpaceMapper;
    private AdCreativeMapper adCreativeMapper;

    @Autowired(required =false)
    public void setAdSpaceMapper(AdSpaceMapper adSpaceMapper) {
        this.adSpaceMapper = adSpaceMapper;
    }

    @Autowired(required =false)
    public void setAdCreativeMapper(AdCreativeMapper adCreativeMapper) {
        this.adCreativeMapper = adCreativeMapper;
    }

    public CustomResult<Object> spaces(String operater, String merchId, RopAdSpaces rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);


        LumosSelective selective = new LumosSelective();
        selective.setFields("Id,Name,SupportFormat,Description");

        List<AdSpace> d_AdSpaces = adSpaceMapper.find(selective);

        List<Object> items = new ArrayList<>();

        for (AdSpace d_AdSpace :
                d_AdSpaces) {

            HashMap<String, Object> item = new HashMap<>();

            item.put("id", d_AdSpace.getId());
            item.put("name", d_AdSpace.getName());
            item.put("supportFormat", d_AdSpace.getSupportFormat());
            item.put("description", d_AdSpace.getDescription());
            items.add(item);
        }

        long total = page.getTotal();
        PageResult<Object> ret = new PageResult<>();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return result.success("", ret);
    }

    public CustomResult<Object> initCreatives(String operater, String merchId, String spaceId) {

        CustomResult<Object> result = new CustomResult<>();
        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("SpaceId",spaceId);

        AdSpace d_AdSpace = adSpaceMapper.findOne(selective);

        if (d_AdSpace == null)
            return result.fail("初始失败");

        HashMap<String,Object> ret=new HashMap<>();

        ret.put("spaceId",d_AdSpace.getId());
        ret.put("spaceName",d_AdSpace.getName());
        ret.put("spaceDescription",d_AdSpace.getDescription());
        ret.put("spaceSupportFormat", d_AdSpace.getSupportFormat());

        return result.success("初始成功",ret);
    }

    public CustomResult<Object> creatives(String operater, String merchId, RopAdCreatives rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);


        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("SpaceId",rop.getSpaceId());
        List<AdCreative> d_AdCreatives = adCreativeMapper.find(selective);

        List<Object> items = new ArrayList<>();

        for (AdCreative d_AdCreative :
                d_AdCreatives) {

            HashMap<String, Object> item = new HashMap<>();

            item.put("id", d_AdCreative.getId());
            item.put("title", d_AdCreative.getTitle());
            item.put("fileUrl", d_AdCreative.getFileUrl());
            item.put("startTime",CommonUtil.toDateStr(d_AdCreative.getStartTime()));
            item.put("endTime",CommonUtil.toDateStr(d_AdCreative.getEndTime()));
            item.put("createTime",CommonUtil.toDateTimeStr(d_AdCreative.getCreateTime()));
            items.add(item);
        }

        long total = page.getTotal();
        PageResult<Object> ret = new PageResult<>();
        ret.setPageNum(pageNum);
        ret.setPageSize(pageSize);
        ret.setTotalPages(page.getPages());
        ret.setTotalSize(total);
        ret.setItems(items);

        return result.success("", ret);
    }

    public CustomResult<Object> initCreativeAdd(String operater, String merchId, String spaceId) {

        CustomResult<Object> result = new CustomResult<>();
        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("SpaceId",spaceId);

        AdSpace d_AdSpace = adSpaceMapper.findOne(selective);

        if (d_AdSpace == null)
            return result.fail("初始失败");

        HashMap<String,Object> ret=new HashMap<>();

        ret.put("spaceId",d_AdSpace.getId());
        ret.put("spaceName",d_AdSpace.getName());
        ret.put("spaceDescription",d_AdSpace.getDescription());
        ret.put("spaceSupportFormat", d_AdSpace.getSupportFormat());

        return result.success("初始成功",ret);

    }

    public CustomResult<Object> creativeAdd(String operater, String merchId, RopAdCreativeAdd rop) {
        CustomResult<Object> result = new CustomResult<>();

        AdCreative d_AdCreative=new AdCreative();
        d_AdCreative.setId(IdWork.generateGUID());
        d_AdCreative.setMerchId(merchId);
        d_AdCreative.setSpaceId(rop.getSpaceId());
        d_AdCreative.setTitle(rop.getTitle());
        d_AdCreative.setFileUrl(rop.getFileUrl());
        d_AdCreative.setStartTime(CommonUtil.toDateTimestamp(rop.getValidDate()[0]));
        d_AdCreative.setEndTime(CommonUtil.toDateTimestamp(rop.getValidDate()[1]));
        d_AdCreative.setPriority(0);
        d_AdCreative.setStatus(1);
        d_AdCreative.setCreator(operater);
        d_AdCreative.setCreateTime(CommonUtil.getDateTimeNow());

       if(adCreativeMapper.insert(d_AdCreative)<=0)
           return result.fail("保存失败");

        return result.success("保存成功");
    }

    public CustomResult<Object> initCreativeEdit(String operater, String merchId, String creativeId) {

        CustomResult<Object> result = new CustomResult<>();
        LumosSelective selective_Creative=new LumosSelective();
        selective_Creative.setFields("*");
        selective_Creative.addWhere("CreativeId",creativeId);

        AdCreative d_AdCreative = adCreativeMapper.findOne(selective_Creative);

        if (d_AdCreative == null)
            return result.fail("初始失败");

        LumosSelective selective_Space=new LumosSelective();
        selective_Space.setFields("*");
        selective_Space.addWhere("SpaceId",d_AdCreative.getSpaceId());

        AdSpace d_AdSpace = adSpaceMapper.findOne(selective_Space);

        if (d_AdSpace == null)
            return result.fail("初始失败");


        HashMap<String,Object> ret=new HashMap<>();

        ret.put("id",d_AdCreative.getId());
        ret.put("title",d_AdCreative.getTitle());
        ret.put("fileUrl",d_AdCreative.getFileUrl());
        ret.put("priority", d_AdCreative.getPriority());
        ret.put("status", d_AdCreative.getStatus());
        ret.put("startTime",CommonUtil.toDateStr(d_AdCreative.getStartTime()));
        ret.put("endTime", CommonUtil.toDateStr(d_AdCreative.getEndTime()));
        ret.put("spaceId",d_AdSpace.getId());
        ret.put("spaceName",d_AdSpace.getName());
        ret.put("spaceDescription",d_AdSpace.getDescription());
        ret.put("spaceSupportFormat", d_AdSpace.getSupportFormat());

        return result.success("初始成功",ret);

    }

    public CustomResult<Object> creativeEdit(String operater, String merchId, RopAdCreativeEdit rop) {
        CustomResult<Object> result = new CustomResult<>();

        AdCreative d_AdCreative=new AdCreative();
        d_AdCreative.setId(rop.getId());
        d_AdCreative.setTitle(rop.getTitle());
        d_AdCreative.setFileUrl(rop.getFileUrl());
        d_AdCreative.setStartTime(CommonUtil.toDateTimestamp(rop.getValidDate()[0]));
        d_AdCreative.setEndTime(CommonUtil.toDateTimestamp(rop.getValidDate()[1]));
        d_AdCreative.setPriority(0);
        d_AdCreative.setStatus(1);
        d_AdCreative.setMender(operater);
        d_AdCreative.setMendTime(CommonUtil.getDateTimeNow());

        if(adCreativeMapper.update(d_AdCreative)<=0)
            return result.fail("保存失败");

        return result.success("保存成功");
    }
}
