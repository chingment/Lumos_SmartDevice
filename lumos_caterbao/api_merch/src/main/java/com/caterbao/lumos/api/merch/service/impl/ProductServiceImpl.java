package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProductListBySpu;
import com.caterbao.lumos.api.merch.rop.model.SkuModel;
import com.caterbao.lumos.api.merch.service.ProductService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.mapper.PrdSkuMapper;
import com.caterbao.lumos.locals.dal.mapper.PrdSpuMapper;
import com.caterbao.lumos.locals.dal.pojo.PrdSku;
import com.caterbao.lumos.locals.dal.pojo.PrdSpu;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private PrdSpuMapper prdSpuMapper;

    @Autowired
    private PrdSkuMapper prdSkuMapper;

    @Override
    public CustomResult listBySpu(String operater, String merchId, RopProductListBySpu rop) {
        CustomResult result = new CustomResult();



        return result;
    }

    @Transactional
    @Override
    public CustomResult add(String operater, String merchId, RopProdcutAdd rop) {
        CustomResult result = new CustomResult();

        if (prdSpuMapper.isExistCumCode(null, merchId, rop.getCumCode()) > 0) {
            return CustomResult.fail("货号已经存在");
        }

        PrdSpu d_PrdSpu = new PrdSpu();
        d_PrdSpu.setId(IdWork.generateGUID());
        d_PrdSpu.setMerchId(merchId);
        d_PrdSpu.setName(rop.getName());
        d_PrdSpu.setPyIdx(CommonUtil.getPyIdxChar(rop.getName()));
        d_PrdSpu.setKindId1(rop.getKindIds().get(0));
        d_PrdSpu.setKindId2(rop.getKindIds().get(1));
        d_PrdSpu.setKindId3(rop.getKindIds().get(2));
        d_PrdSpu.setCumCode(rop.getCumCode());
        d_PrdSpu.setCharTags(JsonUtil.getJson(rop.getCharTags()));
        d_PrdSpu.setBriefDes(rop.getBriefDes());
        d_PrdSpu.setDisplayImgUrls(JsonUtil.getJson(rop.getDisplayImgUrls()));
        d_PrdSpu.setDetailsDes(JsonUtil.getJson(rop.getDetailsDes()));
        d_PrdSpu.setSpecItems(JsonUtil.getJson(rop.getSpecItems()));
        d_PrdSpu.setDelete(false);
        d_PrdSpu.setCreator(operater);
        d_PrdSpu.setCreateTime(CommonUtil.getDateTimeNow());

        long r_PrdSpu_Insert = prdSpuMapper.insert(d_PrdSpu);

        if (r_PrdSpu_Insert <= 0) {
            return CustomResult.success("保存失败");
        }

        for (SkuModel sku : rop.getSkus()) {

            if (prdSkuMapper.isExistCumCode(null, merchId, sku.getCumCode()) > 0) {
                return CustomResult.fail("编码已经存在");
            }

            PrdSku d_PrdSku = new PrdSku();
            d_PrdSku.setId(IdWork.generateGUID());
            d_PrdSku.setMerchId(merchId);
            d_PrdSku.setSpuId(d_PrdSpu.getId());
            d_PrdSku.setName(d_PrdSpu.getName());
            d_PrdSku.setCumCode(sku.getCumCode());
            d_PrdSku.setBarCode(sku.getBarCode());
            d_PrdSku.setSalePrice(sku.getSalePrice());
            d_PrdSku.setPyIdx(d_PrdSpu.getPyIdx());
            d_PrdSku.setDelete(false);
            d_PrdSku.setCreator(operater);
            d_PrdSku.setCreateTime(CommonUtil.getDateTimeNow());

            long r_PrdSku_Insert = prdSkuMapper.insert(d_PrdSku);
            if (r_PrdSku_Insert <= 0) {
                return CustomResult.success("保存失败");
            }
        }

        return CustomResult.success("保存成功");

    }
}
