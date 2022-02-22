package com.caterbao.lumos.api.merch.controller;

import com.caterbao.lumos.locals.biz.model.SkuInfo;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

            Object token_val= redisTemplate.opsForValue().get("token:"+token);
            if(CommonUtil.isEmpty(token_val))
                return null;

            Map<String, Object> map_token_val=JsonUtil.toObject(token_val.toString(),new TypeReference<Map<String, Object> >() {});

            if(map_token_val.containsKey("id"))
                return map_token_val.get("id").toString();

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

            Object token_val= redisTemplate.opsForValue().get("token:"+token);
            if(CommonUtil.isEmpty(token_val))
                return null;

            Map<String, Object> map_token_val=JsonUtil.toObject(token_val.toString(),new TypeReference<Map<String, Object> >() {});

            if(map_token_val.containsKey("merchId"))
                return map_token_val.get("merchId").toString();

            return null;
        }

        return null;
    }
}
