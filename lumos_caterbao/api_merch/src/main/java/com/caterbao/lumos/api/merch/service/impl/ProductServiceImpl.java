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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
    private PrdSkuRfIdMapper prdSkuRfIdMapper;

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

    @Autowired(required = false)
    public void setPrdSkuRfIdMapper(PrdSkuRfIdMapper prdSkuRfIdMapper) {
        this.prdSkuRfIdMapper = prdSkuRfIdMapper;
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

        LumosSelective selective=new LumosSelective();
        selective.setFields("Id,MerchId,CreateTime,DeleteTime");
        selective.addWhere("MerchId",merchId);
        selective.addWhere("IsDelete",rop.getIsDelete());
        selective.addWhere("SpuIds",spuIds);
        List<PrdSpu> d_PrdSpus = prdSpuMapper.find(selective);

        List<Object> items=new ArrayList<>();

        for (PrdSpu d_PrdSpu: d_PrdSpus ) {

            HashMap<String,Object> item=new HashMap<>();

            SpuInfo r_SpuInfo=cacheFactory.getProduct().getSpuInfo(d_PrdSpu.getMerchId(),d_PrdSpu.getId());

            item.put("id",r_SpuInfo.getId());
            item.put("name",r_SpuInfo.getName());
            item.put("cumCode",r_SpuInfo.getCumCode());
            item.put("imgUrl", FileVo.getFirstFileUrl(r_SpuInfo.getDisplayImgUrls()));
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
    }

    @Override
    public CustomResult<Object> init_add(String operater, String merchId) {
        CustomResult<Object> result = new CustomResult<>();

        HashMap<String,Object> ret=new HashMap<>();


        ret.put("sysKinds", prdSysKindMapper.tree());

        return result.success("????????????",ret);
    }

    @Override
    public CustomResult<Object> add(String operater, String merchId, RopProdcutAdd rop) {
        CustomResult<Object> result = new CustomResult<>();
        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {
            if (CommonUtil.isEmpty(rop.getCumCode())) {
                lock.unlock();
                return result.fail("??????????????????");
            }

            if (CommonUtil.isEmpty(rop.getName())) {
                lock.unlock();
                return result.fail("??????????????????");
            }

            if (prdSpuMapper.isExistCumCode(null, merchId, rop.getCumCode()) > 0) {
                lock.unlock();
                return result.fail("??????????????????");
            }

            PrdSpu d_PrdSpu = new PrdSpu();
            if(CommonUtil.isEmpty(rop.getId())) {
                d_PrdSpu.setId(IdWork.buildGuId());
            }
            else{
                d_PrdSpu.setId(rop.getId());
            }
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

           if( prdSpuMapper.insert(d_PrdSpu)<=0) {
               platformTransactionManager.rollback(transaction);
               lock.unlock();
               return result.fail("????????????");
           }

            List<KindAttrVo> sysKindAttrs=rop.getSysKindAttrs();
            if(sysKindAttrs!=null) {
                List<PrdSpuAttr> d_PrdSpuAttrs=new ArrayList<>();

                for (KindAttrVo attr : sysKindAttrs) {
                    PrdSpuAttr d_PrdSpuAttr=new PrdSpuAttr();
                    d_PrdSpuAttr.setId(IdWork.buildLongId());
                    d_PrdSpuAttr.setKindId(attr.getKindId());
                    d_PrdSpuAttr.setSpuId(d_PrdSpu.getId());
                    d_PrdSpuAttr.setAttrId(attr.getId());
                    d_PrdSpuAttr.setAttrValue(attr.getValue());

                    d_PrdSpuAttrs.add(d_PrdSpuAttr);

                }

                prdSpuAttrMapper.insertBatch(d_PrdSpuAttrs);

            }

            for (SkuVo sku : rop.getSkus()) {

                if (CommonUtil.isEmpty(sku.getCumCode())) {
                    lock.unlock();
                    return result.fail("??????????????????");
                }

                if (prdSkuMapper.isExistCumCode(null, merchId, sku.getCumCode()) > 0) {
                    lock.unlock();
                    return result.fail("??????[" + sku.getCumCode() + "]????????????");
                }

                PrdSku d_PrdSku = new PrdSku();

                if(CommonUtil.isEmpty(sku.getId())) {
                    d_PrdSku.setId(IdWork.buildGuId());
                }
                else{
                    d_PrdSku.setId(sku.getId());
                }

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


                if( prdSkuMapper.insert(d_PrdSku)<=0){
                    platformTransactionManager.rollback(transaction);
                    lock.unlock();
                    return result.fail("????????????");
                }

            }

            platformTransactionManager.commit(transaction);
            lock.unlock();

            cacheFactory.getProduct().getSpuInfo(merchId,d_PrdSpu.getId());

            return  result.success("????????????");
        } catch (Exception ex) {
            logger.error("????????????,???????????????",ex);
            platformTransactionManager.rollback(transaction);
            lock.unlock();
            return result.fail("????????????,???????????????");
        }
    }

    @Override
    public CustomResult<Object> init_edit(String operater, String merchId,String spuId) {
        CustomResult<Object> result = new CustomResult<>();

        SpuInfo r_SpuInfo = cacheFactory.getProduct().getSpuInfo(merchId,spuId);

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
            m_Sku.put("isOffSell", r_SkuInfo.getIsOffSell());
            m_Sku.put("specDes", r_SkuInfo.getSpecDes());
            m_Skus.add(m_Sku);
        }

        ret.put("skus", m_Skus);

        LumosSelective selective= new LumosSelective();
        selective.setFields("*");
        selective.addWhere("SpuId", spuId);

        List<Object> m_SysKindAttrs = new ArrayList<>();

        List<PrdSpuAttr> d_PrdSpuAttrs = prdSpuAttrMapper.find(selective);

        for (PrdSpuAttr d_PrdSpuAttr : d_PrdSpuAttrs) {

            selective = new LumosSelective();
            selective.setFields("*");
            selective.addWhere("AttrId", String.valueOf(d_PrdSpuAttr.getAttrId()));

            PrdSysKindAttr d_PrdSysKindAttr = prdSysKindAttrMapper.findOne(selective);

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

        return result.success("????????????", ret);
    }

    @Override
    public CustomResult<Object> edit(String operater, String merchId, RopProdcutEdit rop) {
        CustomResult<Object> result = new CustomResult<>();
        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        try {
            if (CommonUtil.isEmpty(rop.getCumCode())) {
                lock.unlock();
                return result.fail("??????????????????");
            }

            if (CommonUtil.isEmpty(rop.getName())) {
                lock.unlock();
                return result.fail("??????????????????");
            }

            if (prdSpuMapper.isExistCumCode(rop.getId(), merchId, rop.getCumCode()) > 0) {
                lock.unlock();
                return result.fail("??????????????????");
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
                List<PrdSpuAttr> d_PrdSpuAttrs=new ArrayList<>();
                for (KindAttrVo attr : sysKindAttrs) {
                    PrdSpuAttr d_PrdSpuAttr=new PrdSpuAttr();
                    d_PrdSpuAttr.setId(IdWork.buildLongId());
                    d_PrdSpuAttr.setKindId(attr.getKindId());
                    d_PrdSpuAttr.setSpuId(d_PrdSpu.getId());
                    d_PrdSpuAttr.setAttrId(attr.getId());
                    d_PrdSpuAttr.setAttrValue(attr.getValue());

                    d_PrdSpuAttrs.add(d_PrdSpuAttr);
                }

                prdSpuAttrMapper.insertBatch(d_PrdSpuAttrs);
            }

            List<SpecItemVo> specItems=new ArrayList<>();

            for (SkuVo sku: rop.getSkus()) {

                for (SpecDesVo specDes : sku.getSpecDes()) {

                    SpecItemVo o_SpecItem=specItems.stream().filter((SpecItemVo b) ->b.getName().equals(specDes.getName())).findFirst().orElse(null);
                    if(o_SpecItem==null){
                        SpecItemVo specItem=new SpecItemVo();
                        specItem.setName(specDes.getName());
                        SpecItemValueVo specItemValue=new SpecItemValueVo();
                        specItemValue.setName(specDes.getValue());
                        List<SpecItemValueVo> specItemValues=new ArrayList<>();
                        specItemValues.add(specItemValue);
                        specItem.setValue(specItemValues);
                        specItems.add(specItem);
                    }
                    else
                    {
                        SpecItemValueVo o_SpecItemValue= o_SpecItem.getValue().stream().filter((SpecItemValueVo b) ->b.getName().equals(specDes.getValue())).findFirst().orElse(null);
                        if(o_SpecItemValue==null) {
                            o_SpecItem.getValue().add(new SpecItemValueVo(specDes.getValue()));
                        }
                    }
                }
            }

//            for (SkuVo sku: rop.getSkus()) {
//                for (SpecDesVo specDes : sku.getSpecDes()) {
//
//                    boolean isHas = false;
//
//                    for (SpecItemVo specItem : specItems) {
//
//                        if (specItem.getName() == specDes.getName()) {
//
//                            isHas = true;
//
//                            boolean isFlag = false;
//                            for (SpecItemValueVo itemVal : specItem.getValue()) {
//                                if (itemVal.getName() == specDes.getValue()) {
//                                    isFlag = true;
//                                    break;
//                                }
//                            }
//
//                            if (!isFlag) {
//                                List<SpecItemValueVo> itemVals = new ArrayList<>();
//                                SpecItemValueVo itemVal = new SpecItemValueVo();
//                                itemVal.setName(specDes.getValue());
//                                itemVals.add(itemVal);
//                                specItem.setValue(itemVals);
//                            }
//                            break;
//                        }
//                    }
//
//                    if (!isHas) {
//                        SpecItemVo itemModel = new SpecItemVo();
//                        itemModel.setName(specDes.getName());
//                        List<SpecItemValueVo> itemVals = new ArrayList<>();
//                        SpecItemValueVo itemVal = new SpecItemValueVo();
//                        itemVal.setName(specDes.getValue());
//                        itemVals.add(itemVal);
//                        itemModel.setValue(itemVals);
//                    }
//
//                }
//            }


            d_PrdSpu.setSpecItems(JsonUtil.getJson(specItems));
            d_PrdSpu.setMender(operater);
            d_PrdSpu.setMendTime(CommonUtil.getDateTimeNow());

            prdSpuMapper.update(d_PrdSpu);

            for (SkuVo sku : rop.getSkus()) {

                if (CommonUtil.isEmpty(sku.getCumCode())) {
                    lock.unlock();
                    return result.fail("??????????????????");
                }

                if (prdSkuMapper.isExistCumCode(sku.getId(), merchId, sku.getCumCode()) > 0) {
                    lock.unlock();
                    return result.fail("??????????????????");
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
                d_PrdSku.setIsOffSell(sku.getIsOffSell());
                d_PrdSku.setMender(operater);
                d_PrdSku.setMendTime(CommonUtil.getDateTimeNow());

                prdSkuMapper.update(d_PrdSku);

            }

            platformTransactionManager.commit(transaction);
            lock.unlock();

            result=result.success("????????????");

        } catch (Exception ex) {
            logger.error("????????????,???????????????",ex);
            platformTransactionManager.rollback(transaction);
            lock.unlock();
            result = result.fail("????????????,???????????????");
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
                return result.fail("??????ID????????????");
            }

            LumosSelective selective=new LumosSelective();
            selective.setFields("Id,CumCode");
            selective.addWhere("MerchId",merchId);
            selective.addWhere("SpuId",rop.getId());

            PrdSpu d_PrdSpu=prdSpuMapper.findOne(selective);

            d_PrdSpu.setCumCode("old_"+d_PrdSpu.getCumCode());
            d_PrdSpu.setDelete(true);
            d_PrdSpu.setMendTime(CommonUtil.getDateTimeNow());
            d_PrdSpu.setMender(operater);
            d_PrdSpu.setDeleteTime(CommonUtil.getDateTimeNow());
            d_PrdSpu.setDeleter(operater);

            prdSpuMapper.update(d_PrdSpu);

            selective=new LumosSelective();
            selective.setFields("Id,CumCode,SalePrice,BarCode,SpecDes");
            selective.addWhere("MerchId",merchId);
            selective.addWhere("SpuId",rop.getId());
            List<PrdSku> d_PrdSkus=prdSkuMapper.find(selective);

            for (PrdSku d_PrdSku: d_PrdSkus ) {


                d_PrdSku.setCumCode("old_" + d_PrdSku.getCumCode());
                d_PrdSku.setDelete(true);

                prdSkuMapper.update(d_PrdSku);

            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  result.success("????????????");
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return result.fail("????????????,???????????????");
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

    @Override
    public void text_export(String operater,String merchId) {

        try {

            TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

            File file = new File("/Users/chingment/Documents/books.xlsx");
            InputStream is = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0); // ??????????????????
            if (sheet != null) {
                int rowNum = sheet.getLastRowNum(); // ??????????????????????????????
                for (int j = 1; j <= rowNum; j++) {  // ??????????????????????????????,???????????????????????????
                    Row row = sheet.getRow(j); // ???????????????

                    String rfId = row.getCell(0).getStringCellValue();
                    String skuName = row.getCell(1).getStringCellValue();
                    String skuId = row.getCell(2).getStringCellValue();

                    RopProdcutAdd rop = new RopProdcutAdd();
                    rop.setId(skuId);
                    rop.setCumCode("TB" + j);
                    rop.setName(skuName);

                    List<SkuVo> skus = new ArrayList<>();

                    SkuVo sku = new SkuVo();
                    sku.setId(skuId);
                    sku.setBarCode("BAR" + j);
                    sku.setCumCode("TB" + j);
                    sku.setIsOffSell(false);
                    sku.setSalePrice(6);
                    List<SpecDesVo> specDess = new ArrayList<>();
                    SpecDesVo specDes = new SpecDesVo();
                    specDes.setName("?????????");
                    specDes.setValue("???");
                    specDess.add(specDes);
                    sku.setSpecDes(specDess);

                    skus.add(sku);

                    rop.setSkus(skus);

                    List<SpecItemVo> specItems = new ArrayList<>();
                    SpecItemVo specItemVo = new SpecItemVo();
                    specItemVo.setName("?????????");

                    List<SpecItemValueVo> specItemValues = new ArrayList<>();
                    SpecItemValueVo specItemValue = new SpecItemValueVo();
                    specItemValue.setName("???");
                    specItemValues.add(specItemValue);
                    specItemVo.setValue(specItemValues);
                    specItems.add(specItemVo);
                    rop.setSpecItems(specItems);

                    List<Integer> sysKindIds = new ArrayList<>();
                    sysKindIds.add(1713);
                    sysKindIds.add(3258);
                    rop.setSysKindIds(sysKindIds);

                    List<FileVo> displayImgUrls = new ArrayList<>();
                    FileVo filev = new FileVo();
                    filev.setName("default_book.jpg");
                    filev.setUrl("http://file.gddeshang168.com/upload/product/default_book.jpg");
                    displayImgUrls.add(filev);
                    rop.setDisplayImgUrls(displayImgUrls);

                    CustomResult result = add("00000000000000000000000000000000", merchId, rop);

                    if (result.getCode() == 1000) {
                        LumosSelective selective = new LumosSelective();
                        selective.setFields("*");
                        selective.addWhere("MerchId", merchId);
                        selective.addWhere("SkuId", skuId);
                        selective.addWhere("RfId", rfId);
                        PrdSkuRfId prdSkuRfId = prdSkuRfIdMapper.findOne(selective);
                        if (prdSkuRfId == null) {
                            prdSkuRfId = new PrdSkuRfId();
                            prdSkuRfId.setId(IdWork.buildGuId());
                            prdSkuRfId.setMerchId(merchId);
                            prdSkuRfId.setSkuId(skuId);
                            prdSkuRfId.setRfId(rfId);
                            prdSkuRfId.setCreator(operater);
                            prdSkuRfId.setCreateTime(CommonUtil.getDateTimeNow());
                            prdSkuRfIdMapper.insert(prdSkuRfId);
                        }
                    }
                }
            }

            platformTransactionManager.commit(transaction);
        }
        catch (Exception ex){
            logger.error("text_export",ex);
        }
    }

}
