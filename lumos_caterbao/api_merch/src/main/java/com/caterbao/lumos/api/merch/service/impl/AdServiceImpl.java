package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopAdContents;
import com.caterbao.lumos.api.merch.rop.RopAdRelease;
import com.caterbao.lumos.api.merch.rop.RopAdSpaces;
import com.caterbao.lumos.api.merch.service.AdService;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.ImgVo;
import com.caterbao.lumos.locals.common.PageResult;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.AdSpaceMapper;
import com.caterbao.lumos.locals.dal.mapper.SysUserMapper;
import com.caterbao.lumos.locals.dal.pojo.AdSpace;
import com.caterbao.lumos.locals.dal.pojo.Store;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdSpaceMapper adSpaceMapper;

   public CustomResult spaces(String operater, String merchId, RopAdSpaces rop){
       int pageNum = rop.getPageNum();
       int pageSize = rop.getPageSize();


       Page<?> page = PageHelper.startPage(pageNum, pageSize);


       LumosSelective selective=new LumosSelective();
       selective.setFields("Id,Name,SupportFormat,Description");

       List<AdSpace> d_AdSpaces = adSpaceMapper.find(selective);

       List<Object> items=new ArrayList<>();

       for (AdSpace d_AdSpace:
               d_AdSpaces ) {

           HashMap<String,Object> item=new HashMap<>();

           item.put("id",d_AdSpace.getId());
           item.put("name",d_AdSpace.getName());
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

       return CustomResult.success("",ret);
    }
    public   CustomResult release(String operater, String merchId, RopAdRelease rop){
        return  null;
    }
    public  CustomResult contents(String operater, String merchId, RopAdContents rop){
        return  null;
    }
}
