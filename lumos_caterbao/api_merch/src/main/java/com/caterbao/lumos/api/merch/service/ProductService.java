package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopProdcutAdd;
import com.caterbao.lumos.api.merch.rop.RopProdcutDelete;
import com.caterbao.lumos.api.merch.rop.RopProdcutEdit;
import com.caterbao.lumos.api.merch.rop.RopProductList;
import com.caterbao.lumos.locals.common.CustomResult;

public interface ProductService {
    CustomResult list(String operater, String merchId, RopProductList rop);
    CustomResult add(String operater, String merchId, RopProdcutAdd rop);
    CustomResult delete(String operater, String merchId, RopProdcutDelete rop);
    CustomResult init_edit(String operater, String merchId);
    CustomResult edit(String operater, String merchId, RopProdcutEdit rop);
}
