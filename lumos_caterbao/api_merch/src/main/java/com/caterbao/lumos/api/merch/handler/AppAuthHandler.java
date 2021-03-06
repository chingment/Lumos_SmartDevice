package com.caterbao.lumos.api.merch.handler;

import com.caterbao.lumos.api.merch.controller.OwnController;
import com.caterbao.lumos.api.merch.service.ProductService;
import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.Constants;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.TraceLogUtils;
import com.caterbao.lumos.locals.common.web.HttpHelper;
import com.caterbao.lumos.locals.common.web.ResponseReaderHttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@Component
public class AppAuthHandler implements HandlerInterceptor {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    //@Autowired
    //private ProductService productService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        MDC.put(Constants.LOG_TRACE_ID, TraceLogUtils.getTraceId());

        logger.debug("AppAuthHandler.preHandle");

        //productService.text_export("27ab6f924c904e728f1d48543fd2dacc","82854929");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }



        logger.info("请求Url路径 : {}", request.getRequestURL());
        logger.info("请求Url参数 : {}", request.getQueryString());
        logger.info("请求方式    : {}", request.getMethod());

        if(request.getMethod().equals("POST")) {
            String data = HttpHelper.getBodyString(request);
            logger.info("请求体参数 : {}", data);
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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        ResponseReaderHttpServletResponseWrapper responseWrapper = (ResponseReaderHttpServletResponseWrapper) response;
        byte[] responseBody = responseWrapper.getBytes();

        String content = byte2String(responseBody);

        logger.info("请求响应：{}", content);
    }

    public String byte2String(byte[] src) {
        Charset charset = Charset.defaultCharset();
        ByteBuffer buf = ByteBuffer.wrap(src);
        CharBuffer cBuf = charset.decode(buf);

        return  cBuf.toString();
    }
}
