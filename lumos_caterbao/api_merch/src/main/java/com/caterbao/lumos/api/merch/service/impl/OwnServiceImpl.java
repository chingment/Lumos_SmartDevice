package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.merch.service.OwnService;
import com.caterbao.lumos.locals.dal.IdWork;
import com.caterbao.lumos.locals.dal.mapper.UserMapper;
import com.caterbao.lumos.locals.dal.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service

public class OwnServiceImpl implements OwnService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private  RedisTemplate redisTemplate;

    @Autowired
    public OwnServiceImpl() {
    }

    @Override
    public CustomResult loginByAccount(RopOwnLoginByAccount rop) {
        CustomResult result = new CustomResult();

        SysUser d_User = userMapper.findByUserNameAndPassword(rop.getUserName(), rop.getPassword());

        if (d_User == null)
            return CustomResult.fail("账号或密码错误");

        String token = IdWork.generateGUID();

        Map<String, Object> ret = new HashMap<>();
        ret.put("token", token);

        Map<String, Object> token_val = new HashMap<>();
        token_val.put("id", d_User.getId());
        token_val.put("userName", d_User.getUserName());

        redisTemplate.opsForValue().set("token:" + token, token_val);

        return CustomResult.success("登录成功", ret);

    }
}
