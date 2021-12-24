package com.lumos.api.merch.service.impl;

import com.lumos.api.merch.mapper.UserMapper;
import com.lumos.api.merch.pojo.SysUser;
import com.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.lumos.api.merch.service.OwnService;
import com.lumos.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service

public class OwnServiceImpl implements OwnService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public CustomResult loginByAccount(RopOwnLoginByAccount rop) {
        CustomResult result = new CustomResult();

        SysUser d_User = userMapper.findByUserNameAndPassword(rop.getUserName(), rop.getPassword());

        if (d_User == null)
            return CustomResult.fail("账号或密码错误");

        Map<String, Object> ret = new HashMap<>();
        ret.put("token", "123456");

        return CustomResult.success("成功", ret);

    }
}
