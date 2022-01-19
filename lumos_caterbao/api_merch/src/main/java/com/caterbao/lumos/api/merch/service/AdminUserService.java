package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.api.merch.rop.RopAdminUserAdd;
import com.caterbao.lumos.api.merch.rop.RopAdminUserEdit;
import com.caterbao.lumos.api.merch.rop.RopAdminUserList;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.stereotype.Service;

@Service
public interface AdminUserService {

    CustomResult init_add(String operater, String merchId);
    CustomResult add(String operater, String merchId, RopAdminUserAdd rop);
    CustomResult list(String operater, String merchId, RopAdminUserList rop);
    CustomResult init_edit(String operater, String merchId, String shopId);
    CustomResult edit(String operater, String merchId, RopAdminUserEdit rop);
}
