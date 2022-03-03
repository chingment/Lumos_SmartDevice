package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.*;
import com.caterbao.lumos.api.merch.service.IcCardService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.PageResult;
import com.caterbao.lumos.locals.common.PasswordUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.IcCardMapper;
import com.caterbao.lumos.locals.dal.pojo.IcCard;
import com.caterbao.lumos.locals.dal.pojo.SysMerchUser;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class IcCardServiceImpl implements IcCardService {

    private IcCardMapper icCardMapper;

    @Autowired(required = false)
    public void setIcCardMapper(IcCardMapper icCardMapper) {
        this.icCardMapper = icCardMapper;
    }

    @Override
    public CustomResult<Object> list(String operater, String merchId, RopIcCardList rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective=new LumosSelective();
        selective.setFields("*");
        selective.addWhere("MerchId",merchId);

        List<IcCard> d_IcCards = icCardMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (IcCard d_IcCard:
                d_IcCards ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_IcCard.getId());
            item.put("fullName",d_IcCard.getFullName());
            item.put("cardNo",d_IcCard.getCardNo());
            item.put("isDisable",d_IcCard.getDisable());
            item.put("createTime", CommonUtil.toDateTimeStr(d_IcCard.getCreateTime()));
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
    public CustomResult<Object>  init_add(String operater, String merchId) {
        CustomResult<Object> result = new CustomResult<>();
        return result.success("");
    }

    @Override
    public CustomResult<Object>  add(String operater, String merchId, RopIcCardAdd rop) {

        CustomResult<Object> result = new CustomResult<>();

        return result.success("");
    }

    @Override
    public CustomResult<Object> init_edit(String operater, String merchId, String userId) {

        CustomResult<Object> result = new CustomResult<>();
        return result.success("");
    }

    @Override
    public CustomResult<Object> edit(String operater, String merchId, RopIcCardEdit rop) {

        CustomResult<Object> result = new CustomResult<>();
        return result.success("");
    }
}
