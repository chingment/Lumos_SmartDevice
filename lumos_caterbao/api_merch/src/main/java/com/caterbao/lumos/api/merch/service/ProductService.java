package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProdcutDelete;
import com.caterbao.lumos.api.merch.rop.RopProdcutEdit;
import com.caterbao.lumos.api.merch.rop.RopProductList;
import com.caterbao.lumos.locals.common.CustomResult;

public interface ProductService {
    CustomResult<Object> list(String operater, String merchId, RopProductList rop);

    CustomResult<Object> init_add(String operater, String merchId);

    CustomResult<Object> add(String operater, String merchId, RopProdcutAdd rop);

    CustomResult<Object> delete(String operater, String merchId, RopProdcutDelete rop);

    CustomResult<Object> init_edit(String operater, String merchId, String spuId);

    CustomResult<Object> edit(String operater, String merchId, RopProdcutEdit rop);

    CustomResult<Object> getSysKindAttrs(String operater, String merchId, String kindIds);

    CustomResult<Object> searchSpu(String operater,String merchId,String key);

    void text_export(String operater,String merchId);
}
