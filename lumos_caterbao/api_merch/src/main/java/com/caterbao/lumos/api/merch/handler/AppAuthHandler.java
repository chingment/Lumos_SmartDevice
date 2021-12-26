package com.caterbao.lumos.api.merch.handler;

import com.alibaba.druid.util.StringUtils;
import com.caterbao.lumos.locals.common.CustomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AppAuthHandler implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        System.out.println("AppAuthHandler.preHandle");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String token = request.getHeader("token");
        Object token_val=null;

        if(!StringUtils.isEmpty(token)) {
            token_val = redisTemplate.opsForValue().get("token:" + token);
        }

        if(token_val==null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            CustomResult result = new CustomResult();
            result.setCode(2501);
            result.setMsg("登录状态已超时");
            String str_result = result.toJSONString();
            response.getWriter().append(str_result);
            return false;
        }
        else
        {
            return true;
        }
    }
}
