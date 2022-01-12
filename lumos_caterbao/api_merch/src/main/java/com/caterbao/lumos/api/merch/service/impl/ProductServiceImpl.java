package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProductListBySpu;
import com.caterbao.lumos.api.merch.service.ProductService;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.dal.mapper.PrdSpuMapper;
import com.caterbao.lumos.locals.dal.pojo.PrdSpu;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private PrdSpuMapper productMapper;

    @Override
    public CustomResult listBySpu(String operater, String merchId, RopProductListBySpu rop) {
        CustomResult result = new CustomResult();


        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        PageHelper.startPage(pageNum, pageSize);

        List<PrdSpu> sysMenus = productMapper.getSpus();

        return result;
    }

    @Override
    public CustomResult add(String operater, String merchId, RopProdcutAdd rop) {
        CustomResult result = new CustomResult();


        return CustomResult.success("保存成功");
    }
}
