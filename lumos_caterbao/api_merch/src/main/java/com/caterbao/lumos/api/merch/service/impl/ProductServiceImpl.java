package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProdcutDelete;
import com.caterbao.lumos.api.merch.rop.RopProdcutEdit;
import com.caterbao.lumos.api.merch.rop.RopProductList;
import com.caterbao.lumos.api.merch.rop.model.SkuModel;
import com.caterbao.lumos.api.merch.service.ProductService;
import com.caterbao.lumos.locals.biz.cache.CacheFactory;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.PrdSkuMapper;
import com.caterbao.lumos.locals.dal.mapper.PrdSpuMapper;
import com.caterbao.lumos.locals.dal.mapper.SysPrdKindMapper;
import com.caterbao.lumos.locals.dal.pojo.PrdSku;
import com.caterbao.lumos.locals.dal.pojo.PrdSpu;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

    private PrdSpuMapper prdSpuMapper;
    private PrdSkuMapper prdSkuMapper;
    private PlatformTransactionManager platformTransactionManager;
    private TransactionDefinition transactionDefinition;
    private CacheFactory cacheFactory;
    private SysPrdKindMapper sysPrdKindMapper;

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
    public void setSysPrdKindMapper(SysPrdKindMapper sysPrdKindMapper) {
        this.sysPrdKindMapper = sysPrdKindMapper;
    }

    private Lock lock = new ReentrantLock();

    @Override
    public CustomResult<Object> list(String operater, String merchId, RopProductList rop) {
        CustomResult<Object> result = new CustomResult<>();

        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective_PrdSpu=new LumosSelective();
        selective_PrdSpu.setFields("Id,Name,CumCode,DisplayImgUrls,CreateTime");
        selective_PrdSpu.addWhere("MerchId",merchId);
        selective_PrdSpu.addWhere("IsDelete",rop.getIsDelete());
        List<PrdSpu> d_PrdSpus = prdSpuMapper.find(selective_PrdSpu);

        List<Object> items=new ArrayList<>();

        for (PrdSpu d_PrdSpu:
                d_PrdSpus ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_PrdSpu.getId());
            item.put("name",d_PrdSpu.getName());
            item.put("cumCode",d_PrdSpu.getCumCode());
            item.put("imgUrl", ImgVo.getMainImgUrl(d_PrdSpu.getDisplayImgUrls()));
            item.put("sysKinds",getSysKinds(d_PrdSpu.getSysKindIds()));
            LumosSelective selective_PrdSku=new LumosSelective();
            selective_PrdSku.setFields("Id,CumCode,BarCode,SalePrice,SpecDes");
            selective_PrdSku.addWhere("MerchId",merchId);
            selective_PrdSku.addWhere("SpuId",d_PrdSpu.getId());
            List<PrdSku> d_PrdSkus = prdSkuMapper.find(selective_PrdSku);

            List<Object> skus=new ArrayList<>();

            for (PrdSku d_PrdSku: d_PrdSkus ) {
                HashMap<String,Object> item_sku=new HashMap<>();
                item_sku.put("id",d_PrdSku.getId());
                item_sku.put("cumCode",d_PrdSku.getCumCode());
                item_sku.put("barCode",d_PrdSku.getBarCode());
                item_sku.put("salePrice",d_PrdSku.getSalePrice());
                item_sku.put("specDes",d_PrdSku.getSpecDes());
                skus.add(item_sku);
            }

            item.put("skus",skus);

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


        ret.put("sysKinds",sysPrdKindMapper.tree());

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
            d_PrdSpu.setId(IdWork.generateGUID());
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

            for (SkuModel sku : rop.getSkus()) {


                if (CommonUtil.isEmpty(sku.getCumCode())) {
                    lock.unlock();
                    return result.fail("编码不能为空");
                }

                if (prdSkuMapper.isExistCumCode(null, merchId, sku.getCumCode()) > 0) {
                    lock.unlock();
                    return result.fail("编码已经存在");
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
                d_PrdSku.setSpecDes(JsonUtil.getJson(sku.getSpecDes()));
                d_PrdSku.setSpecIdx(SpecDesModel.GetIdx(sku.getSpecDes()));
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
            return  result.success("保存成功");
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return result.fail("保存失败,服务器异常");
        }
    }

    @Override
    public CustomResult<Object> init_edit(String operater, String merchId,String spuId) {
        CustomResult<Object> result = new CustomResult<>();
        LumosSelective selective_PrdSpu=new LumosSelective();
        selective_PrdSpu.setFields("Id,Name,CumCode,SysKindIds,SpecItems, DisplayImgUrls,CharTags,BriefDes,DetailsDes");
        selective_PrdSpu.addWhere("MerchId",merchId);
        selective_PrdSpu.addWhere("SpuId",spuId);

        PrdSpu d_PrdSpu=prdSpuMapper.findOne(selective_PrdSpu);

        LumosSelective selective_PrdSkus=new LumosSelective();
        selective_PrdSkus.setFields("Id,CumCode,SalePrice,BarCode,SpecDes");
        selective_PrdSkus.addWhere("MerchId",merchId);
        selective_PrdSkus.addWhere("SpuId",spuId);

        List<PrdSku> d_PrdSkus=prdSkuMapper.find(selective_PrdSkus);

        HashMap<String,Object> ret=new HashMap<>();
        ret.put("id",d_PrdSpu.getId());
        ret.put("name",d_PrdSpu.getName());
        ret.put("cumCode",d_PrdSpu.getCumCode());
        ret.put("isUnityUpdate",false);
        ret.put("sysKindIds",CommonUtil.intStr2Arr(d_PrdSpu.getSysKindIds()));
        ret.put("specItems",JsonUtil.toObject(d_PrdSpu.getSpecItems()));
        ret.put("displayImgUrls",JsonUtil.toObject(d_PrdSpu.getDisplayImgUrls()));
        ret.put("charTags",JsonUtil.toObject(d_PrdSpu.getCharTags()));
        ret.put("briefDes",d_PrdSpu.getBriefDes());
        ret.put("detailsDes",JsonUtil.toObject(d_PrdSpu.getDetailsDes()));

        List<Object> m_Skus=new ArrayList<>();

        for (PrdSku d_PrdSku: d_PrdSkus ) {

            HashMap<String, Object> m_Sku = new HashMap<>();
            m_Sku.put("id", d_PrdSku.getId());
            m_Sku.put("cumCode", d_PrdSku.getCumCode());
            m_Sku.put("salePrice",d_PrdSku.getSalePrice());
            m_Sku.put("barCode",d_PrdSku.getBarCode());
            m_Sku.put("isOffSell",false);
            m_Sku.put("specDes",JsonUtil.toObject(d_PrdSku.getSpecDes()));
            m_Skus.add(m_Sku);
        }

        ret.put("skus",m_Skus);
        ret.put("sysKinds",sysPrdKindMapper.tree());
        return result.success("初始成功",ret);
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

            List<SpecItemModel> specItems=new ArrayList<>();


            for (SkuModel sku: rop.getSkus()) {
                for (SpecDesModel specDes : sku.getSpecDes()) {

                    Optional<SpecItemModel> s=specItems.stream().filter((SpecItemModel b) ->b.getName()==specDes.getName()).findFirst();



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

            for (SkuModel sku : rop.getSkus()) {

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
                d_PrdSku.setSpecIdx(SpecDesModel.GetIdx(sku.getSpecDes()));
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

            cacheFactory.getProduct().removeSpuInfo(merchId,rop.getId());

            return  result.success("保存成功");
        } catch (Exception ex) {
            platformTransactionManager.rollback(transaction);
            ex.printStackTrace();
            lock.unlock();
            return result.fail("保存失败,服务器异常");
        }
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

            long r_PrdSpu_Update=prdSpuMapper.update(d_PrdSpu);
            if (r_PrdSpu_Update <= 0) {
                lock.unlock();
                return result.fail("保存失败");
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
                    return result.fail("保存失败");
                }
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  result.success("保存成功");
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return result.fail("保存失败,服务器异常");
        }
    }


    private FieldModel getSysKinds(String sysKinds) {
        FieldModel model = new FieldModel("1,2,3", "图书");

        return model;
    }

}
