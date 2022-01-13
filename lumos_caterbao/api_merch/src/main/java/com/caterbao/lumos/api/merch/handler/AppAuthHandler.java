package com.caterbao.lumos.api.merch.handler;

import com.caterbao.lumos.api.merch.controller.OwnController;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class AppAuthHandler implements HandlerInterceptor {

    public static Logger logger = LoggerFactory.getLogger(OwnController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        logger.debug("AppAuthHandler.preHandle");


        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        String token = request.getHeader("token");
        Object token_val=null;

        if(!CommonUtil.isEmpty(token)) {
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
            redisTemplate.expire("token:" + token,1, TimeUnit.HOURS);

            return true;
        }
    }
}
