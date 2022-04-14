package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProdcutDelete;
import com.caterbao.lumos.api.merch.rop.RopProdcutEdit;
import com.caterbao.lumos.api.merch.rop.RopProductList;
import com.caterbao.lumos.api.merch.rop.vo.KindAttrVo;
import com.caterbao.lumos.api.merch.rop.vo.SkuVo;
import com.caterbao.lumos.api.merch.service.ProductService;
import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.biz.model.SpuInfo;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.common.vo.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.*;
import com.caterbao.lumos.locals.dal.pojo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysql.cj.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ProductServiceImpl implements ProductService {


    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    private PrdSpuMapper prdSpuMapper;
    private PrdSkuMapper prdSkuMapper;
    private PrdSpuAttrMapper prdSpuAttrMapper;
    private PlatformTransactionManager platformTransactionManager;
    private TransactionDefinition transactionDefinition;
    private CacheFactory cacheFactory;
    private PrdSysKindMapper prdSysKindMapper;
    private PrdSysKindAttrMapper prdSysKindAttrMapper;

    @Autowired(required = false)
    public void setPrdSpuMapper(PrdSpuMapper prdSpuMapper) {
        this.prdSpuMapper = prdSpuMapper;
    }

    @Autowired(required = false)
    public void setPrdSkuMapper(PrdSkuMapper prdSkuMapper) {
        this.prdSkuMapper = prdSkuMapper;
    }

    @Autowired(required = false)
    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Autowired(required = false)
    public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
        this.transactionDefinition = transactionDefinition;
    }

    @Autowired(required = false)
    public void setCacheFactory(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    @Autowired(required = false)
    public void setPrdSysKindMapper(PrdSysKindMapper prdSysKindMapper) {
        this.prdSysKindMapper = prdSysKindMapper;
    }

    @Autowired(required = false)
    public void setPrdSysKindAttrMapper(PrdSysKindAttrMapper prdSysKindAttrMapper) {
        this.prdSysKindAttrMapper = prdSysKindAttrMapper;
    }

    @Autowired(required = false)
    public void setPrdSpuAttrMapper(PrdSpuAttrMapper prdSpuAttrMapper) {
        this.prdSpuAttrMapper = prdSpuAttrMapper;
    }

    private Lock lock = new ReentrantLock();

    @Override
    public CustomResult<Object> list(String operater, String merchId, RopProductList rop) {

        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();

        List<String> spuIds = new ArrayList<>();
        if(!CommonUtil.isEmpty(rop.getKeyWord())) {
            spuIds = cacheFactory.getProduct().searchSpuIds(merchId, rop.getKeyWord());
            if (spuIds.size() == 0) {
                spuIds.add("_");
            }
        }

        Page<?> page = PageHelper.startPage(pageNum, pageSize,"CreateTime DESC");

        LumosSelective selective_PrdSpu=new LumosSelective();
        selective_PrdSpu.setFields("Id,MerchId,CreateTime,DeleteTime");
        selective_PrdSpu.addWhere("MerchId",merchId);
        selective_PrdSpu.addWhere("IsDelete",rop.getIsDelete());
        selective_PrdSpu.addWhere("SpuIds",spuIds);
        List<PrdSpu> d_PrdSpus = prdSpuMapper.find(selective_PrdSpu);

        List<Object> items=new ArrayList<>();

        for (PrdSpu d_PrdSpu: d_PrdSpus ) {

            HashMap<String,Object> item=new HashMap<>();

            SpuInfo r_SpuInfo=cacheFactory.getProduct().getSpuInfo(d_PrdSpu.getMerchId(),d_PrdSpu.getId());

            item.put("id",r_SpuInfo.getId());
            item.put("name",r_SpuInfo.getName());
            item.put("cumCode",r_SpuInfo.getCumCode());
            item.put("imgUrl", FileVo.getFileUrl(r_SpuInfo.getDisplayImgUrls()));
            item.put("createTime",CommonUtil.toDateTimeStr(d_PrdSpu.getCreateTime()));
            item.put("deleteTime",CommonUtil.toDateTimeStr(d_PrdSpu.getDeleteTime()));
            item.put("sysKinds",getSysKinds(r_SpuInfo.getSysKindIds()));

            List<Object> m_Skus=new ArrayList<>();


            List<SpecIdxSkuVo> specIdxSkus= r_SpuInfo.getSpecIdxSkus();
            for (SpecIdxSkuVo specIdxSku : specIdxSkus) {

                SkuInfo r_SkuInfo = cacheFactory.getProduct().getSkuInfo(merchId, specIdxSku.getSkuId());

                HashMap<String, Object> m_Sku = new HashMap<>();
                m_Sku.put("id", r_SkuInfo.getId());
                m_Sku.put("cumCode", r_SkuInfo.getCumCode());
                m_Sku.put("salePrice", r_SkuInfo.getSalePrice());
                m_Sku.put("barCode", r_SkuInfo.getBarCode());
                m_Sku.put("isOffSell", false);
                m_Sku.put("specDes", r_SkuInfo.getSpecDes());
                m_Skus.add(m_Sku);
            }

            item.put("skus",m_Skus);

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


//        CustomResult<Object> result = new CustomResult<>();
//
//        int pageNum = rop.getPageNum();
//        int pageSize = rop.getPageSize();
//
//
//        Page<?> page = PageHelper.startPage(pageNum, pageSize,"CreateTime DESC");
//
//        LumosSelective selective_PrdSpu=new LumosSelective();
//        selective_PrdSpu.setFields("Id,Name,CumCode,SysKindIds,DisplayImgUrls,CreateTime");
//        selective_PrdSpu.addWhere("MerchId",merchId);
//        selective_PrdSpu.addWhere("IsDelete",rop.getIsDelete());
//        List<PrdSpu> d_PrdSpus = prdSpuMapper.find(selective_PrdSpu);
//
//        List<Object> items=new ArrayList<>();
//
//        for (PrdSpu d_PrdSpu:
//                d_PrdSpus ) {
//
//            HashMap<String,Object> item=new HashMap<>();
//
//            item.put("id",d_PrdSpu.getId());
//            item.put("name",d_PrdSpu.getName());
//            item.put("cumCode",d_PrdSpu.getCumCode());
//            item.put("imgUrl", ImgVo.getMainImgUrl(d_PrdSpu.getDisplayImgUrls()));
//            item.put("sysKinds",getSysKinds(d_PrdSpu.getSysKindIds()));
//            LumosSelective selective_PrdSku=new LumosSelective();
//            selective_PrdSku.setFields("Id,CumCode,BarCode,SalePrice,SpecDes");
//            selective_PrdSku.addWhere("MerchId",merchId);
//            selective_PrdSku.addWhere("SpuId",d_PrdSpu.getId());
//            List<PrdSku> d_PrdSkus = prdSkuMapper.find(selective_PrdSku);
//
//            List<Object> skus=new ArrayList<>();
//
//            for (PrdSku d_PrdSku: d_PrdSkus ) {
//                HashMap<String,Object> item_sku=new HashMap<>();
//                item_sku.put("id",d_PrdSku.getId());
//                item_sku.put("cumCode",d_PrdSku.getCumCode());
//                item_sku.put("barCode",d_PrdSku.getBarCode());
//                item_sku.put("salePrice",d_PrdSku.getSalePrice());
//                item_sku.put("specDes",d_PrdSku.getSpecDes());
//                skus.add(item_sku);
//            }
//
//            item.put("skus",skus);
//
//            items.add(item);
//        }
//
//        long total = page.getTotal();
//        PageResult<Object> ret = new PageResult<>();
//        ret.setPageNum(pageNum);
//        ret.setPageSize(pageSize);
//        ret.setTotalPages(page.getPages());
//        ret.setTotalSize(total);
//        ret.setItems(items);
//
//        return result.success("",ret);
    }

    @Override
    public CustomResult<Object> init_add(String operater, String merchId) {
        CustomResult<Object> result = new CustomResult<>();

        HashMap<String,Object> ret=new HashMap<>();


        ret.put("sysKinds", prdSysKindMapper.tree());

        return result.success("初始成功",ret);
    }

    @Override
    public CustomResult<Object> add(String operater, String merchId, RopProdcutAdd rop) {
        CustomResult<Object> result = new CustomResult<>();
        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {
            if (CommonUtil.isEmpty(rop.getCumCode())) {
                lock.unlock();
                return result.fail("货号不能为空");
            }

            if (CommonUtil.isEmpty(rop.getName())) {
                lock.unlock();
                return result.fail("名称不能为空");
            }

            if (prdSpuMapper.isExistCumCode(null, merchId, rop.getCumCode()) > 0) {
                lock.unlock();
                return result.fail("货号已经存在");
            }

            PrdSpu d_PrdSpu = new PrdSpu();
            d_PrdSpu.setId(IdWork.buildGuId());
            d_PrdSpu.setMerchId(merchId);
            d_PrdSpu.setName(rop.getName());
            d_PrdSpu.setPyIdx(CommonUtil.getPyIdxChar(rop.getName()));
            d_PrdSpu.setSysKindIds(CommonUtil.intArr2Str(rop.getSysKindIds()));
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
                lock.unlock();
                return result.fail("保存失败");
            }

            //
            List<KindAttrVo> sysKindAttrs=rop.getSysKindAttrs();
            if(sysKindAttrs!=null) {
                for (KindAttrVo attr : sysKindAttrs) {
                    PrdSpuAttr d_PrdSpuAttr=new PrdSpuAttr();
                    d_PrdSpuAttr.setId(IdWork.buildLongId());
                    d_PrdSpuAttr.setKindId(attr.getKindId());
                    d_PrdSpuAttr.setSpuId(d_PrdSpu.getId());
                    d_PrdSpuAttr.setAttrId(attr.getId());
                    d_PrdSpuAttr.setAttrValue(attr.getValue());
                    if (prdSpuAttrMapper.insert(d_PrdSpuAttr) <= 0) {
                        lock.unlock();
                        return result.fail("保存失败");
                    }
                }
            }

            for (SkuVo sku : rop.getSkus()) {


                if (CommonUtil.isEmpty(sku.getCumCode())) {
                    lock.unlock();
                    return result.fail("编码不能为空");
                }

                if (prdSkuMapper.isExistCumCode(null, merchId, sku.getCumCode()) > 0) {
                    lock.unlock();
                    return result.fail("编码["+sku.getCumCode()+"]已经存在");
                }

                PrdSku d_PrdSku = new PrdSku();
                d_PrdSku.setId(IdWork.buildGuId());
                d_PrdSku.setMerchId(merchId);
                d_PrdSku.setSpuId(d_PrdSpu.getId());
                d_PrdSku.setName(d_PrdSpu.getName());
                d_PrdSku.setCumCode(sku.getCumCode());
                d_PrdSku.setBarCode(sku.getBarCode());
                d_PrdSku.setSalePrice(sku.getSalePrice());
                d_PrdSku.setPyIdx(d_PrdSpu.getPyIdx());
                d_PrdSku.setSpecDes(JsonUtil.getJson(sku.getSpecDes()));
                d_PrdSku.setSpecIdx(SpecDesVo.GetIdx(sku.getSpecDes()));
                d_PrdSku.setDelete(false);
                d_PrdSku.setCreator(operater);
                d_PrdSku.setCreateTime(CommonUtil.getDateTimeNow());

                long r_PrdSku_Insert = prdSkuMapper.insert(d_PrdSku);
                if (r_PrdSku_Insert <= 0) {
                    lock.unlock();
                    return result.fail("保存失败");
                }
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();

            cacheFactory.getProduct().getSpuInfo(merchId,d_PrdSpu.getId());

            return  result.success("保存成功");
        } catch (Exception ex) {
            logger.error("保存失败,服务器异常",ex);
            platformTransactionManager.rollback(transaction);
            lock.unlock();
            return result.fail("保存失败,服务器异常");
        }
    }

    @Override
    public CustomResult<Object> init_edit(String operater, String merchId,String spuId) {
        CustomResult<Object> result = new CustomResult<>();

        SpuInfo r_SpuInfo = cacheFactory.getProduct().getSpuInfo(merchId,spuId);

        //LumosSelective selective_PrdSpu = new LumosSelective();
        //selective_PrdSpu.setFields("Id,Name,CumCode,SysKindIds,SpecItems, DisplayImgUrls,CharTags,BriefDes,DetailsDes");
        //selective_PrdSpu.addWhere("MerchId", merchId);
        //selective_PrdSpu.addWhere("SpuId", spuId);

        //PrdSpu d_PrdSpu = prdSpuMapper.findOne(selective_PrdSpu);

        //LumosSelective selective_PrdSkus = new LumosSelective();
        //selective_PrdSkus.setFields("Id,CumCode,SalePrice,BarCode,SpecDes");
        //selective_PrdSkus.addWhere("MerchId", merchId);
        //selective_PrdSkus.addWhere("SpuId", spuId);

        //List<PrdSku> d_PrdSkus = prdSkuMapper.find(selective_PrdSkus);

        HashMap<String, Object> ret = new HashMap<>();
        ret.put("id", r_SpuInfo.getId());
        ret.put("name", r_SpuInfo.getName());
        ret.put("cumCode", r_SpuInfo.getCumCode());
        ret.put("isUnityUpdate", false);
        ret.put("sysKindIds", CommonUtil.intStr2Arr(r_SpuInfo.getSysKindIds()));
        ret.put("specItems", r_SpuInfo.getSpecItems());
        ret.put("displayImgUrls", r_SpuInfo.getDisplayImgUrls());
        ret.put("charTags", r_SpuInfo.getCharTags());
        ret.put("briefDes", r_SpuInfo.getBriefDes());
        ret.put("detailsDes", r_SpuInfo.getDetailsDes());

        List<Object> m_Skus = new ArrayList<>();

        List<SpecIdxSkuVo> specIdxSkus= r_SpuInfo.getSpecIdxSkus();
        for (SpecIdxSkuVo specIdxSku : specIdxSkus) {

            SkuInfo r_SkuInfo = cacheFactory.getProduct().getSkuInfo(merchId, specIdxSku.getSkuId());

            HashMap<String, Object> m_Sku = new HashMap<>();
            m_Sku.put("id", r_SkuInfo.getId());
            m_Sku.put("cumCode", r_SkuInfo.getCumCode());
            m_Sku.put("salePrice", r_SkuInfo.getSalePrice());
            m_Sku.put("barCode", r_SkuInfo.getBarCode());
            m_Sku.put("isOffSell", false);
            m_Sku.put("specDes", r_SkuInfo.getSpecDes());
            m_Skus.add(m_Sku);
        }

        ret.put("skus", m_Skus);

        LumosSelective selective_PrdSpuAttrs = new LumosSelective();
        selective_PrdSpuAttrs.setFields("*");
        selective_PrdSpuAttrs.addWhere("SpuId", spuId);

        List<Object> m_SysKindAttrs = new ArrayList<>();

        List<PrdSpuAttr> d_PrdSpuAttrs = prdSpuAttrMapper.find(selective_PrdSpuAttrs);

        for (PrdSpuAttr d_PrdSpuAttr : d_PrdSpuAttrs) {

            LumosSelective selective_PrdSysKindAttrs = new LumosSelective();
            selective_PrdSysKindAttrs.setFields("*");
            selective_PrdSysKindAttrs.addWhere("AttrId", String.valueOf(d_PrdSpuAttr.getAttrId()));

            PrdSysKindAttr d_PrdSysKindAttr = prdSysKindAttrMapper.findOne(selective_PrdSysKindAttrs);

            if (d_PrdSysKindAttr != null) {
                HashMap<String, Object> m_SysKindAttr = new HashMap<>();
                m_SysKindAttr.put("id", d_PrdSpuAttr.getAttrId());
                m_SysKindAttr.put("name", d_PrdSysKindAttr.getName());
                m_SysKindAttr.put("kindId", d_PrdSpuAttr.getKindId());
                m_SysKindAttr.put("value", d_PrdSpuAttr.getAttrValue());
                m_SysKindAttr.put("min", d_PrdSysKindAttr.getMin());
                m_SysKindAttr.put("max", d_PrdSysKindAttr.getMax());
                m_SysKindAttr.put("required", d_PrdSysKindAttr.isRequired());
                m_SysKindAttrs.add(m_SysKindAttr);
            }

        }

        ret.put("sysKindAttrs", m_SysKindAttrs);

        ret.put("sysKinds", prdSysKindMapper.tree());

        return result.success("初始成功", ret);
    }

    @Override
    public CustomResult<Object> edit(String operater, String merchId, RopProdcutEdit rop) {
        CustomResult<Object> result = new CustomResult<>();
        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {
            if (CommonUtil.isEmpty(rop.getCumCode())) {
                lock.unlock();
                return result.fail("货号不能为空");
            }

            if (CommonUtil.isEmpty(rop.getName())) {
                lock.unlock();
                return result.fail("名称不能为空");
            }

            if (prdSpuMapper.isExistCumCode(rop.getId(), merchId, rop.getCumCode()) > 0) {
                lock.unlock();
                return result.fail("货号已经存在");
            }

            PrdSpu d_PrdSpu = new PrdSpu();
            d_PrdSpu.setId(rop.getId());
            d_PrdSpu.setName(rop.getName());
            d_PrdSpu.setPyIdx(CommonUtil.getPyIdxChar(rop.getName()));
            d_PrdSpu.setSysKindIds(CommonUtil.intArr2Str(rop.getSysKindIds()));
            d_PrdSpu.setCumCode(rop.getCumCode());
            d_PrdSpu.setCharTags(JsonUtil.getJson(rop.getCharTags()));
            d_PrdSpu.setBriefDes(rop.getBriefDes());
            d_PrdSpu.setDisplayImgUrls(JsonUtil.getJson(rop.getDisplayImgUrls()));
            d_PrdSpu.setDetailsDes(JsonUtil.getJson(rop.getDetailsDes()));


            prdSpuAttrMapper.deleteBySpuId(d_PrdSpu.getId());

            List<KindAttrVo> sysKindAttrs=rop.getSysKindAttrs();
            if(sysKindAttrs!=null) {
                for (KindAttrVo attr : sysKindAttrs) {
                    PrdSpuAttr d_PrdSpuAttr=new PrdSpuAttr();
                    d_PrdSpuAttr.setId(IdWork.buildLongId());
                    d_PrdSpuAttr.setKindId(attr.getKindId());
                    d_PrdSpuAttr.setSpuId(d_PrdSpu.getId());
                    d_PrdSpuAttr.setAttrId(attr.getId());
                    d_PrdSpuAttr.setAttrValue(attr.getValue());
                    if (prdSpuAttrMapper.insert(d_PrdSpuAttr) <= 0) {
                        lock.unlock();
                        return result.fail("保存失败");
                    }
                }
            }

            List<SpecItemVo> specItems=new ArrayList<>();

            for (SkuVo sku: rop.getSkus()) {
                for (SpecDesVo specDes : sku.getSpecDes()) {

                    Optional<SpecItemVo> s=specItems.stream().filter((SpecItemVo b) ->b.getName()==specDes.getName()).findFirst();



                }
            }

//            boolean isHas = false;
//            for (SpecItemModel specItem : specItems) {
//                if (specItem.getName() == specDes.getName()) {
//                    isHas = true;
//
//                    boolean isFlag = false;
//                    for (SpecItemValueModel itemVal : specItem.getValue()) {
//                        if (itemVal.getName() == specDes.getValue()) {
//                            isFlag = true;
//                        }
//                    }
//
//                    if (!isFlag) {
//                        List<SpecItemValueModel> itemVals = new ArrayList<>();
//                        SpecItemValueModel itemVal = new SpecItemValueModel();
//                        itemVal.setName(specDes.getValue());
//                        itemVals.add(itemVal);
//                        specItem.setValue(itemVals);
//                    }
//
//                    break;
//                }
//            }
//
//            if (!isHas) {
//                SpecItemModel itemModel = new SpecItemModel();
//                itemModel.setName(specDes.getName());
//                List<SpecItemValueModel> itemVals = new ArrayList<>();
//                SpecItemValueModel itemVal = new SpecItemValueModel();
//                itemVal.setName(specDes.getValue());
//                itemVals.add(itemVal);
//                itemModel.setValue(itemVals);
//            }


            d_PrdSpu.setSpecItems(JsonUtil.getJson(specItems));

            d_PrdSpu.setMender(operater);
            d_PrdSpu.setMendTime(CommonUtil.getDateTimeNow());

            long r_PrdSpu_Update = prdSpuMapper.update(d_PrdSpu);

            if (r_PrdSpu_Update <= 0) {
                lock.unlock();
                return result.fail("保存失败");
            }

            for (SkuVo sku : rop.getSkus()) {

                if (CommonUtil.isEmpty(sku.getCumCode())) {
                    lock.unlock();
                    return result.fail("编码不能为空");
                }

                if (prdSkuMapper.isExistCumCode(sku.getId(), merchId, sku.getCumCode()) > 0) {
                    lock.unlock();
                    return result.fail("编码已经存在");
                }

                PrdSku d_PrdSku = new PrdSku();
                d_PrdSku.setId(sku.getId());
                d_PrdSku.setName(d_PrdSpu.getName());
                d_PrdSku.setCumCode(sku.getCumCode());
                d_PrdSku.setBarCode(sku.getBarCode());
                d_PrdSku.setSalePrice(sku.getSalePrice());
                d_PrdSku.setPyIdx(d_PrdSpu.getPyIdx());
                d_PrdSku.setSpecDes(JsonUtil.getJson(sku.getSpecDes()));
                d_PrdSku.setSpecIdx(SpecDesVo.GetIdx(sku.getSpecDes()));
                d_PrdSku.setMender(operater);
                d_PrdSku.setMendTime(CommonUtil.getDateTimeNow());

                long r_PrdSku_Update= prdSkuMapper.update(d_PrdSku);
                if (r_PrdSku_Update <= 0) {
                    lock.unlock();
                    return result.fail("保存失败");
                }
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();

            result=result.success("保存成功");
        } catch (Exception ex) {
            logger.error("保存失败,服务器异常",ex);
            platformTransactionManager.rollback(transaction);
            lock.unlock();
            result = result.fail("保存失败,服务器异常");
        }

        if(result.getCode()==1000) {
            cacheFactory.getProduct().removeSpuInfo(merchId, rop.getId());
            cacheFactory.getProduct().getSpuInfo(merchId, rop.getId());
        }

        return  result;

    }

    @Override
    public CustomResult<Object> delete(String operater, String merchId, RopProdcutDelete rop) {
        CustomResult<Object> result = new CustomResult<>();
        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            if (CommonUtil.isEmpty(rop.getId())) {
                lock.unlock();
                return result.fail("货号ID不能为空");
            }


            LumosSelective selective_PrdSpu=new LumosSelective();
            selective_PrdSpu.setFields("Id,CumCode");
            selective_PrdSpu.addWhere("MerchId",merchId);
            selective_PrdSpu.addWhere("SpuId",rop.getId());
            PrdSpu d_PrdSpu=prdSpuMapper.findOne(selective_PrdSpu);

            d_PrdSpu.setCumCode("backup_"+d_PrdSpu.getCumCode());
            d_PrdSpu.setDelete(true);
            d_PrdSpu.setMendTime(CommonUtil.getDateTimeNow());
            d_PrdSpu.setMender(operater);
            d_PrdSpu.setDeleteTime(CommonUtil.getDateTimeNow());
            d_PrdSpu.setDeleter(operater);
            long r_PrdSpu_Update=prdSpuMapper.update(d_PrdSpu);
            if (r_PrdSpu_Update <= 0) {
                lock.unlock();
                return result.fail("回收失败");
            }

            LumosSelective selective_PrdSkus=new LumosSelective();
            selective_PrdSkus.setFields("Id,CumCode,SalePrice,BarCode,SpecDes");
            selective_PrdSkus.addWhere("MerchId",merchId);
            selective_PrdSkus.addWhere("SpuId",rop.getId());
            List<PrdSku> d_PrdSkus=prdSkuMapper.find(selective_PrdSkus);

            for (PrdSku d_PrdSku: d_PrdSkus ) {


                d_PrdSku.setCumCode("backup_"+d_PrdSku.getCumCode());
                d_PrdSku.setDelete(true);

                long r_PrdSku_Update= prdSkuMapper.update(d_PrdSku);
                if (r_PrdSku_Update <= 0) {
                    lock.unlock();
                    return result.fail("回收失败");
                }
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  result.success("回收成功");
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return result.fail("回收失败,服务器异常");
        }
    }

    @Override
    public CustomResult<Object> getSysKindAttrs(String operater, String merchId, String ids) {
        CustomResult<Object> result = new CustomResult<>();

        LumosSelective selective = new LumosSelective();
        selective.setFields("*");
        selective.addWhere("KindId", "1713");
        selective.addWhere("IsUse", "1");

        List<PrdSysKindAttr> d_PrdSysKindAttrs = prdSysKindAttrMapper.find(selective);

        List<Object> attrs = new ArrayList<>();

        for (PrdSysKindAttr d_PrdSysKindAttr : d_PrdSysKindAttrs) {
            HashMap<String, Object> attr = new HashMap<>();
            attr.put("id", d_PrdSysKindAttr.getId());
            attr.put("kindId", 1713);
            attr.put("name", d_PrdSysKindAttr.getName());
            attr.put("min", d_PrdSysKindAttr.getMin());
            attr.put("max", d_PrdSysKindAttr.getMax());
            attr.put("required", d_PrdSysKindAttr.isRequired());
            attr.put("value", null);
            attrs.add(attr);
        }

        HashMap<String, Object> ret = new HashMap<>();
        ret.put("attrs", attrs);
        return result.success("", ret);
    }

    @Override
    public CustomResult<Object> searchSpu(String operater, String merchId, String key) {
        CustomResult<Object> result = new CustomResult<>();

        HashMap<String, Object> ret = new HashMap<>();

        List<SpuInfo> spus=new ArrayList<>();

        if(!CommonUtil.isEmpty(key)) {
            spus = cacheFactory.getProduct().searchSpu(merchId, key);
        }

        ret.put("spus", spus);

        return result.success("", ret);
    }


    private FieldVo getSysKinds(String sysKinds) {
        FieldVo model = new FieldVo();

        List<Integer> sysKindIds = CommonUtil.intStr2Arr(sysKinds);

        if (sysKindIds == null)
            return model;

        if (sysKindIds.size() == 0)
            return model;

        int id = sysKindIds.get(sysKindIds.size() - 1);

        List<PrdSysKind> d_PrdSysKinds = prdSysKindMapper.findParentById(id);

        List<String> names = new ArrayList<>();
        for (PrdSysKind d_PrdSysKind : d_PrdSysKinds) {
            names.add(d_PrdSysKind.getName());
        }

        model.setText(CommonUtil.arr2Str(names));

        return model;
    }

}
