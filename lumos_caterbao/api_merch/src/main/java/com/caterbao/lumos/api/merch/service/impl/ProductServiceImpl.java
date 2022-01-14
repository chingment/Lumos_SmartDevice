package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProdcutDelete;
import com.caterbao.lumos.api.merch.rop.RopProdcutEdit;
import com.caterbao.lumos.api.merch.rop.RopProductList;
import com.caterbao.lumos.api.merch.rop.model.SkuModel;
import com.caterbao.lumos.api.merch.rop.model.SpecDesModel;
import com.caterbao.lumos.api.merch.rop.model.SpecItemModel;
import com.caterbao.lumos.api.merch.rop.model.SpecItemValueModel;
import com.caterbao.lumos.api.merch.service.ProductService;
import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.LumosSelective;
import com.caterbao.lumos.locals.dal.mapper.PrdSkuMapper;
import com.caterbao.lumos.locals.dal.mapper.PrdSpuMapper;
import com.caterbao.lumos.locals.dal.pojo.PrdSku;
import com.caterbao.lumos.locals.dal.pojo.PrdSpu;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private PrdSpuMapper prdSpuMapper;

    @Autowired
    private PrdSkuMapper prdSkuMapper;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    private Lock lock = new ReentrantLock();

    @Override
    public CustomResult list(String operater, String merchId, RopProductList rop) {
        int pageNum = rop.getPageNum();
        int pageSize = rop.getPageSize();


        Page<?> page = PageHelper.startPage(pageNum, pageSize);

        LumosSelective selective_PrdSpu=new LumosSelective();
        selective_PrdSpu.setFields("Id,Name,CumCode,DisplayImgUrls,CreateTime");
        selective_PrdSpu.addWhere("MerchId",merchId);

        List<PrdSpu> d_PrdSpus = prdSpuMapper.find(selective_PrdSpu);

        List<Object> items=new ArrayList<>();

        for (PrdSpu d_PrdSpu:
                d_PrdSpus ) {

            HashMap<String,Object> item=new HashMap<>();

            item.put("id",d_PrdSpu.getId());
            item.put("name",d_PrdSpu.getName());
            item.put("cumCode",d_PrdSpu.getCumCode());
            item.put("imgUrl", ImgVo.getMainImgUrl(d_PrdSpu.getDisplayImgUrls()));

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

        return CustomResult.success("",ret);
    }

    @Override
    public CustomResult add(String operater, String merchId, RopProdcutAdd rop) {
        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        CustomResult result = new CustomResult();
        try {
            if (CommonUtil.isEmpty(rop.getCumCode()))
                return CustomResult.fail("货号不能为空");

            if (CommonUtil.isEmpty(rop.getName()))
                return CustomResult.fail("名称不能为空");

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
                return CustomResult.fail("保存失败");
            }

            for (SkuModel sku : rop.getSkus()) {


                if (CommonUtil.isEmpty(sku.getCumCode())) {
                    return CustomResult.fail("编码不能为空");
                }

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
                d_PrdSku.setSpecDes(JsonUtil.getJson(sku.getSpecDes()));
                d_PrdSku.setSpecIdx(SpecDesModel.GetIdx(sku.getSpecDes()));
                d_PrdSku.setDelete(false);
                d_PrdSku.setCreator(operater);
                d_PrdSku.setCreateTime(CommonUtil.getDateTimeNow());

                long r_PrdSku_Insert = prdSkuMapper.insert(d_PrdSku);
                if (r_PrdSku_Insert <= 0) {
                    return CustomResult.fail("保存失败");
                }
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  CustomResult.success("保存成功");
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return CustomResult.fail("保存失败,服务器异常");
        }
    }

    @Override
    public CustomResult init_edit(String operater, String merchId,String spuId) {

        LumosSelective selective_PrdSpu=new LumosSelective();
        selective_PrdSpu.setFields("Id,Name,CumCode,KindId1,KindId2,KindId3,SpecItems, DisplayImgUrls,CharTags,BriefDes,DetailsDes");
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
        List<Integer> kindIds=new ArrayList<>();
        kindIds.add(d_PrdSpu.getKindId1());
        kindIds.add(d_PrdSpu.getKindId2());
        kindIds.add(d_PrdSpu.getKindId3());

        ret.put("kindIds",kindIds);
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

        return CustomResult.success("初始成功",ret);
    }

    @Override
    public CustomResult edit(String operater, String merchId, RopProdcutEdit rop) {
        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        CustomResult result = new CustomResult();
        try {
            if (CommonUtil.isEmpty(rop.getCumCode()))
                return CustomResult.fail("货号不能为空");

            if (CommonUtil.isEmpty(rop.getName()))
                return CustomResult.fail("名称不能为空");

            if (prdSpuMapper.isExistCumCode(rop.getId(), merchId, rop.getCumCode()) > 0) {
                return CustomResult.fail("货号已经存在");
            }

            PrdSpu d_PrdSpu = new PrdSpu();
            d_PrdSpu.setId(rop.getId());
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

            long r_PrdSpu_Upate = prdSpuMapper.update(d_PrdSpu);

            if (r_PrdSpu_Upate <= 0) {
                return CustomResult.fail("保存失败");
            }

            for (SkuModel sku : rop.getSkus()) {

                if (CommonUtil.isEmpty(sku.getCumCode())) {
                    return CustomResult.fail("编码不能为空");
                }

                if (prdSkuMapper.isExistCumCode(sku.getId(), merchId, sku.getCumCode()) > 0) {
                    return CustomResult.fail("编码已经存在");
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
                    return CustomResult.fail("保存失败");
                }
            }

            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  CustomResult.success("保存成功");
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return CustomResult.fail("保存失败,服务器异常");
        }
    }

    @Override
    public CustomResult delete(String operater, String merchId, RopProdcutDelete rop) {
        lock.lock();
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);
        CustomResult result = new CustomResult();
        try {
            if (CommonUtil.isEmpty(rop.getId()))
                return CustomResult.fail("货号ID不能为空");


            platformTransactionManager.commit(transaction);
            lock.unlock();
            return  CustomResult.success("保存成功");
        } catch (Exception e) {
            platformTransactionManager.rollback(transaction);
            e.printStackTrace();
            lock.unlock();
            return CustomResult.fail("保存失败,服务器异常");
        }
    }
}
