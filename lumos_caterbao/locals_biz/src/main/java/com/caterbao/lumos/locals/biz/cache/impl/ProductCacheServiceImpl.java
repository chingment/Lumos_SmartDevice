package com.caterbao.lumos.locals.biz.cache.impl;

import com.caterbao.lumos.locals.biz.cache.ProductCacheService;
import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.PrdSkuMapper;
import com.caterbao.lumos.locals.dal.mapper.PrdSkuRfIdMapper;
import com.caterbao.lumos.locals.dal.pojo.PrdSku;
import com.caterbao.lumos.locals.dal.pojo.PrdSkuRfId;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
class ProductCacheServiceImpl implements ProductCacheService {

    private String CACHE_KEY_SKU_RFID_PRE="prd_sku_rfid";
    private String CACHE_KEY_SKU_INFO_PRE="prd_sku_info";

    private PrdSkuMapper prdSkuMapper;
    private PrdSkuRfIdMapper prdSkuRfIdMapper;
    private RedisTemplate redisTemplate;

    @Autowired(required =false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired(required =false)
    public void setPrdSkuMapper(PrdSkuMapper prdSkuMapper) {
        this.prdSkuMapper = prdSkuMapper;
    }

    @Autowired(required =false)
    public void setPrdSkuRfIdMapper(PrdSkuRfIdMapper prdSkuRfIdMapper) {
        this.prdSkuRfIdMapper = prdSkuRfIdMapper;
    }

    private String getSkuIdByRfId(String merchId,String rfId) {
        String skuId = "";

        Object r_SkuId= redisTemplate.opsForHash().get(CACHE_KEY_SKU_RFID_PRE + ":" + merchId, rfId);

        if (CommonUtil.isEmpty(r_SkuId)) {

            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("MerchId", merchId);
            selective.addWhere("RfId", rfId);
            PrdSkuRfId d_PrdSkuRfId = prdSkuRfIdMapper.findOne(selective);
            if(d_PrdSkuRfId==null) {
                return skuId;
            }
            else {
                skuId=d_PrdSkuRfId.getSkuId();
                redisTemplate.opsForHash().put(CACHE_KEY_SKU_RFID_PRE + ":" + merchId,rfId,skuId);
            }
        }
        else {
            skuId = r_SkuId.toString();
        }

        return skuId;
    }

    @Override
    public SkuInfo getSkuInfoByRfId(String merchId,String rfId) {

        SkuInfo skuInfo = new SkuInfo();


        String skuId = getSkuIdByRfId(merchId, rfId);

        if(CommonUtil.isEmpty(skuId)) {
            return skuInfo;
        }
        else
        {
            Object r_SkuInfo= redisTemplate.opsForHash().get(CACHE_KEY_SKU_INFO_PRE + ":" + merchId, skuId);

            if(CommonUtil.isEmpty(r_SkuInfo)) {

                LumosSelective selective = new LumosSelective();
                selective.setFields("*");
                selective.addWhere("MerchId", merchId);
                selective.addWhere("SkuId", skuId);
                PrdSku d_PrdSku = prdSkuMapper.findOne(selective);
                if (d_PrdSku == null)
                    return skuInfo;

                skuInfo.setId(d_PrdSku.getId());
                skuInfo.setName(d_PrdSku.getName());
                skuInfo.setSalePrice(d_PrdSku.getSalePrice());
                skuInfo.setPyIdx(d_PrdSku.getPyIdx());
                skuInfo.setSpuId(d_PrdSku.getSpuId());
                skuInfo.setBarCode(d_PrdSku.getBarCode());
                skuInfo.setCumCode(d_PrdSku.getCumCode());

                redisTemplate.opsForHash().put(CACHE_KEY_SKU_INFO_PRE + ":" + merchId,skuId, JsonUtil.getJson(skuInfo));

            }
            else {
                skuInfo = JsonUtil.toObject(r_SkuInfo.toString(), new TypeReference<SkuInfo>() {
                });
            }
        }


        return skuInfo;
    }
}
