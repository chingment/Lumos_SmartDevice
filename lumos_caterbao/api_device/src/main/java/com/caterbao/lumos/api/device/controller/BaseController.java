package com.caterbao.lumos.api.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class BaseController {

    private HttpServletRequest request;
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getCurrentUserId() {

        return null;
    }

    public String getCurrentMerchId() {

        return null;
    }
}
