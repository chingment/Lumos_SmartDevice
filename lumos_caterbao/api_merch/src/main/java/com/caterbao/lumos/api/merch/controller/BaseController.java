package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.locals.common.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class BaseController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate redisTemplate;

    public String getCurrentUserId() {

        //根据redis缓存获取用户Id
        if (request != null) {
            String token = request.getHeader("Token");

            if(CommonUtil.isEmpty(token)) {
                return null;
            }

            Map<String, Object> token_val= (Map<String, Object>)redisTemplate.opsForValue().get("token:"+token);

            if(token_val.containsKey("id"))
                return token_val.get("id").toString();

            return null;
        }

        return null;
    }

    public String getCurrentMerchId() {

        //根据redis缓存获取用户Id
        if (request != null) {
            String token = request.getHeader("Token");

            if(CommonUtil.isEmpty(token)) {
                return null;
            }

            Map<String, Object> token_val= (Map<String, Object>)redisTemplate.opsForValue().get("token:"+token);

            if(token_val.containsKey("merchId"))
                return token_val.get("merchId").toString();

            return null;
        }

        return null;
    }
}
