package com.caterbao.lumos.api.merch.service.impl;

import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.api.merch.rop.RopOwnLoginByAccount;
import com.caterbao.lumos.api.merch.service.OwnService;
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
       // this.userMapper = userMapper;
       // this.redisTemplate = redisTemplate;
    }

    @Override
    public CustomResult loginByAccount(RopOwnLoginByAccount rop) {
        CustomResult result = new CustomResult();

        SysUser d_User = userMapper.findByUserNameAndPassword(rop.getUserName(), rop.getPassword());

        if (d_User == null)
            return CustomResult.fail("账号或密码错误");

        Map<String, Object> ret = new HashMap<>();
        ret.put("token", "123456");

        redisTemplate.opsForValue().set("aaaa",ret);

        Object obj= redisTemplate.opsForValue().get("aaaa");

        return CustomResult.success("成功", ret);

    }
}
