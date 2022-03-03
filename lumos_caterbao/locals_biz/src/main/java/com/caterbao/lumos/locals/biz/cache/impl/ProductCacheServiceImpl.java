package com.caterbao.lumos.locals.biz.cache.impl;

import com.caterbao.lumos.locals.biz.cache.ProductCacheService;
import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.biz.model.SpuInfo;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.PrdSkuMapper;
import com.caterbao.lumos.locals.dal.mapper.PrdSkuRfIdMapper;
import com.caterbao.lumos.locals.dal.mapper.PrdSpuMapper;
import com.caterbao.lumos.locals.dal.pojo.PrdSku;
import com.caterbao.lumos.locals.dal.pojo.PrdSkuRfId;
import com.caterbao.lumos.locals.dal.pojo.PrdSpu;
import com.fasterxml.jackson.core.type.TypeReference;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
class ProductCacheServiceImpl implements ProductCacheService {

    private String CACHE_KEY_SKU_RFID_PRE="prd_sku_rfid";
    private String CACHE_KEY_SKU_INFO_PRE="prd_sku_info";
    private String CACHE_KEY_SKU_SKEY_PRE="prd_sku_skey";
    private String CACHE_KEY_SPU_INFO_PRE="prd_spu_info";
    private String CACHE_KEY_SPU_SKEY_PRE="prd_spu_skey";
    private PrdSkuMapper prdSkuMapper;
    private PrdSpuMapper prdSpuMapper;
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
    public void setPrdSpuMapper(PrdSpuMapper prdSpuMapper) {
        this.prdSpuMapper = prdSpuMapper;
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

        if (CommonUtil.isEmpty(skuId)) {
            return skuInfo;
        } else {
            return getSkuInfo(merchId, skuId);


        }
    }

    @Override
    public SkuInfo getSkuInfo(String merchId,String skuId) {

        SkuInfo skuInfo = new SkuInfo();

        Object r_SkuInfo = redisTemplate.opsForHash().get(CACHE_KEY_SKU_INFO_PRE + ":" + merchId, skuId);

        if (CommonUtil.isEmpty(r_SkuInfo)) {

            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("MerchId", merchId);
            selective.addWhere("SkuId", skuId);
            PrdSku d_PrdSku = prdSkuMapper.findOne(selective);
            if (d_PrdSku == null)
                return skuInfo;

            LumosSelective selective_PrdSpu = new LumosSelective();
            selective_PrdSpu.setFields("*");
            selective_PrdSpu.addWhere("MerchId", merchId);
            selective_PrdSpu.addWhere("SpuId", d_PrdSku.getSpuId());
            PrdSpu d_PrdSpu = prdSpuMapper.findOne(selective_PrdSpu);

            if (d_PrdSpu == null)
                return skuInfo;

            skuInfo.setId(d_PrdSku.getId());
            skuInfo.setName(d_PrdSku.getName());
            skuInfo.setSalePrice(d_PrdSku.getSalePrice());
            skuInfo.setPyIdx(d_PrdSku.getPyIdx());
            skuInfo.setSpuId(d_PrdSku.getSpuId());
            skuInfo.setBarCode(d_PrdSku.getBarCode());
            skuInfo.setCumCode(d_PrdSku.getCumCode());

            List<ImgVo> displayImgUrls = JsonUtil.toObject(d_PrdSpu.getDisplayImgUrls(), new TypeReference<List<ImgVo>>() {
            });
            if (displayImgUrls != null && displayImgUrls.size() > 0) {
                skuInfo.setImgUrl(displayImgUrls.get(0).getUrl());
            }

            skuInfo.setSpecIdx(d_PrdSku.getSpecIdx());


            if (!CommonUtil.isEmpty(skuInfo.getPyIdx())) {
                redisTemplate.opsForHash().put(CACHE_KEY_SKU_SKEY_PRE + ":" + merchId, "BR:" + skuInfo.getBarCode()+":"+skuId, skuId);
            }

            if (!CommonUtil.isEmpty(skuInfo.getPyIdx())) {
                redisTemplate.opsForHash().put(CACHE_KEY_SKU_SKEY_PRE + ":" + merchId, "PY:" + skuInfo.getPyIdx()+":"+skuId, skuId);
            }

            if (!CommonUtil.isEmpty(skuInfo.getName())) {
                redisTemplate.opsForHash().put(CACHE_KEY_SKU_SKEY_PRE + ":" + merchId, "NA:" + skuInfo.getName()+":"+skuId, skuId);
            }

            if (!CommonUtil.isEmpty(skuInfo.getCumCode())) {
                redisTemplate.opsForHash().put(CACHE_KEY_SKU_SKEY_PRE + ":" + merchId, "CC:" + skuInfo.getCumCode()+":"+skuId, skuId);
            }


            redisTemplate.opsForHash().put(CACHE_KEY_SKU_INFO_PRE + ":" + merchId, skuId, JsonUtil.getJson(skuInfo));

        } else {
            skuInfo = JsonUtil.toObject(r_SkuInfo.toString(), new TypeReference<SkuInfo>() {
            });
        }

        return skuInfo;
    }

    @Override
    public SpuInfo getSpuInfo(String merchId,String spuId){

        SpuInfo spuInfo = new SpuInfo();

        Object r_SpuInfo = redisTemplate.opsForHash().get(CACHE_KEY_SPU_INFO_PRE + ":" + merchId, spuId);

        if (CommonUtil.isEmpty(r_SpuInfo)) {

            LumosSelective selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("MerchId", merchId);
            selective.addWhere("SpuId", spuId);
            PrdSpu d_PrdSpu = prdSpuMapper.findOne(selective);
            if (d_PrdSpu == null)
                return spuInfo;

            spuInfo.setId(d_PrdSpu.getId());
            spuInfo.setName(d_PrdSpu.getName());
            spuInfo.setPyIdx(d_PrdSpu.getPyIdx());
            spuInfo.setCumCode(d_PrdSpu.getCumCode());
            spuInfo.setBriefDes(d_PrdSpu.getBriefDes());
            spuInfo.setCharTags(JsonUtil.toObject(d_PrdSpu.getCharTags(), new TypeReference<List<String>>() {}));
            spuInfo.setDetailsDes(JsonUtil.toObject(d_PrdSpu.getDetailsDes(), new TypeReference<List<ImgVo>>() {}));
            spuInfo.setDisplayImgUrls(JsonUtil.toObject(d_PrdSpu.getDisplayImgUrls(), new TypeReference<List<ImgVo>>() {}));
            spuInfo.setSpecItems(JsonUtil.toObject(d_PrdSpu.getSpecItems(), new TypeReference<List<SpecItemModel>>() {}));

            LumosSelective selective_PrdSku = new LumosSelective();
            selective_PrdSku.setFields("*");
            selective_PrdSku.addWhere("MerchId", merchId);
            selective_PrdSku.addWhere("SpuId", spuId);
            List<PrdSku> d_PrdSkus = prdSkuMapper.find(selective_PrdSku);
            if (d_PrdSkus != null) {

                List<SpecIdxSkuModel> specIdxSkus = new ArrayList<>();

                for (PrdSku prdSku : d_PrdSkus) {
                    SkuInfo r_SkuInfo = getSkuInfo(merchId, prdSku.getId());
                    SpecIdxSkuModel specIdxSku = new SpecIdxSkuModel();
                    specIdxSku.setSkuId(prdSku.getId());
                    specIdxSku.setSpecIdx(prdSku.getSpecIdx());
                    specIdxSkus.add(specIdxSku);
                }

                spuInfo.setSpecIdxSkus(specIdxSkus);
            }

            if(!CommonUtil.isEmpty(spuInfo.getPyIdx())){
                redisTemplate.opsForHash().put(CACHE_KEY_SPU_SKEY_PRE + ":" + merchId, "PY:"+spuInfo.getPyIdx()+":"+spuId, spuId);
            }

            if(!CommonUtil.isEmpty(spuInfo.getName())){
                redisTemplate.opsForHash().put(CACHE_KEY_SPU_SKEY_PRE + ":" + merchId, "NA:"+spuInfo.getName()+":"+spuId, spuId);
            }

            if(!CommonUtil.isEmpty(spuInfo.getCumCode())){
                redisTemplate.opsForHash().put(CACHE_KEY_SPU_SKEY_PRE + ":" + merchId, "CC:"+spuInfo.getCumCode()+":"+spuId, spuId);
            }

            redisTemplate.opsForHash().put(CACHE_KEY_SPU_INFO_PRE + ":" + merchId, spuId, JsonUtil.getJson(spuInfo));

        } else {
            spuInfo = JsonUtil.toObject(r_SpuInfo.toString(), new TypeReference<SpuInfo>() {
            });
        }


        return spuInfo;
    }


    @Override
    public void removeSpuInfo(String merchId,String spuId) {

        SpuInfo r_Spu = getSpuInfo(merchId, spuId);

        if (r_Spu != null) {
            if (r_Spu.getSpecIdxSkus() != null) {
                for (SpecIdxSkuModel specIdxSku : r_Spu.getSpecIdxSkus()) {
                    redisTemplate.opsForHash().delete(CACHE_KEY_SKU_INFO_PRE + ":" + merchId, specIdxSku.getSkuId());


                    Cursor<Map.Entry<Object,Object>> cursor = redisTemplate.opsForHash().scan(CACHE_KEY_SKU_SKEY_PRE + ":" + merchId, ScanOptions.scanOptions().match("*:*:"+specIdxSku.getSkuId()).build());
                    while (cursor.hasNext()){
                        Map.Entry<Object,Object> entry = cursor.next();
                        redisTemplate.opsForHash().delete(CACHE_KEY_SKU_SKEY_PRE + ":" + merchId,entry.getKey());
                    }



                }
            }
        }

        redisTemplate.opsForHash().delete(CACHE_KEY_SPU_INFO_PRE + ":" + merchId, spuId);


        Cursor<Map.Entry<Object,Object>> cursor = redisTemplate.opsForHash().scan(CACHE_KEY_SPU_SKEY_PRE + ":" + merchId, ScanOptions.scanOptions().match("*:*:"+spuId).build());
        while (cursor.hasNext()){
            Map.Entry<Object,Object> entry = cursor.next();
            redisTemplate.opsForHash().delete(CACHE_KEY_SPU_SKEY_PRE + ":" + merchId,entry.getKey());
        }


    }


    @Override
    public List<SpuInfo> searchSpu(String merchId, String key) {
        List<SpuInfo> m_SpuInfos = new ArrayList<>();

        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(CACHE_KEY_SPU_SKEY_PRE + ":" + merchId, ScanOptions.scanOptions().match("*" + key + "*").build());

        int i = 0;
        while (cursor.hasNext() && i < 10) {
            Map.Entry<Object, Object> entry = cursor.next();

            Object r_SpuInfo = redisTemplate.opsForHash().get(CACHE_KEY_SPU_INFO_PRE + ":" + merchId, entry.getValue().toString());
            if (r_SpuInfo != null) {
                SpuInfo spuInfo = JsonUtil.toObject(r_SpuInfo.toString(), new TypeReference<SpuInfo>() {
                });
                if (spuInfo != null) {
                    m_SpuInfos.add(spuInfo);
                }
            }
        }

        return m_SpuInfos;
    }

//    public void SpuSKey(String merchId,String keyPre,String keyIdx, String spuId) {
//
//        Object skey_val = redisTemplate.opsForHash().get(CACHE_KEY_SPU_SKEY_PRE + ":" + merchId, keyPre + ":" + keyIdx);
//
//        List<String> arr_vals = new ArrayList<>();
//        if (skey_val == null) {
//            arr_vals.add(spuId);
//        } else {
//            arr_vals = JsonUtil.toObject(skey_val.toString(), new TypeReference<List<String>>() {
//            });
//            if (arr_vals != null) {
//                if (arr_vals.contains(spuId)) {
//                    arr_vals.add(spuId);
//                }
//            }
//        }
//
//        redisTemplate.opsForHash().put(CACHE_KEY_SPU_SKEY_PRE + ":" + merchId, keyPre + ":" + keyIdx, JsonUtil.getJson(arr_vals));
//    }
}
