package com.caterbao.lumos.locals.biz.service.impl;

import com.caterbao.lumos.locals.biz.service.ProductService;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.PrdSkuMapper;
import com.caterbao.lumos.locals.dal.pojo.PrdSku;
import com.caterbao.lumos.locals.dal.pojo.PrdSpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private PrdSkuMapper prdSkuMapper;


    @Autowired(required =false)
    public void setPrdSkuMapper(PrdSkuMapper prdSkuMapper) {
        this.prdSkuMapper = prdSkuMapper;
    }

    @Override
    public void getRfIdSkuInfo() {

        LumosSelective selective_PrdSku=new LumosSelective();
        selective_PrdSku.setFields("*");
        selective_PrdSku.addWhere("MerchId","82854929");
        selective_PrdSku.addWhere("SkuId","c2fa8417da5c41af88edfe3fc8efe3fe");
        PrdSku d_PrdSku = prdSkuMapper.findOne(selective_PrdSku);
        String a="dasdd";
    }
}
