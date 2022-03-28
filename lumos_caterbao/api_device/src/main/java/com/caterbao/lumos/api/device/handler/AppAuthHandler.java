package com.caterbao.lumos.api.device.handler;

import com.caterbao.lumos.locals.common.*;
import com.caterbao.lumos.locals.common.web.HttpHelper;
import com.caterbao.lumos.locals.common.web.ResponseReaderHttpServletResponseWrapper;
import com.caterbao.lumos.locals.dal.mapper.AppSoftMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppAuthHandler implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AppSoftMapper appSoftMapper;

    @Autowired
    public void setAppSoftMapper(AppSoftMapper appSoftMapper) {
        this.appSoftMapper = appSoftMapper;
    }

    private CustomResult result = new CustomResult();

    private HttpServletResponse response;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        MDC.put(Constants.LOG_TRACE_ID, TraceLogUtils.getTraceId());

        logger.info("AppAuthHandler.preHandle");

        logger.info("请求Url : {}", request.getRequestURL().toString());
        logger.info("请求方式 : {}", request.getMethod());


        this.response = response;

        String contentType = request.getContentType();
        String method = request.getMethod();
        if (!method.equals("POST")) {
            String str_result = getResponse(2501, "请求Method不正确");
            response.getWriter().append(str_result);
            return false;
        }

        String appId = request.getHeader("appId");
        String appKey = request.getHeader("appKey");
        String sign = request.getHeader("sign");
        String timestamp = request.getHeader("timestamp");

        if (appId == null || appKey == null || sign == null || timestamp == null) {
            String str_result = getResponse(2501, "缺少必要参数");
            response.getWriter().append(str_result);
            return false;
        }

        String appSecret = appSoftMapper.getSecretByKey(appKey);

        if (appSecret == null) {
            String str_result = getResponse(2501, "应用无效");
            response.getWriter().append(str_result);
            return false;
        }

        String data="";

        if(contentType.contains("application/json")) {
             data = HttpHelper.getBodyStringByApplicationJson(request);
        }
        else if(contentType.contains("multipart/form-data")){
            //排除上传的文件的文件流数据
            data = HttpHelper.getBodyStringByMultipartFormData(request);
        }

        logger.info("请求参数 : {}",data);

        String my_sign = getSign(appId, appKey, appSecret, data, timestamp);

        if (!sign.equals(my_sign)) {
            String str_result = getResponse(2501, "签名错误");
            response.getWriter().append(str_result);
            return false;
        }

        return true;
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


    private String getResponse(int code,String msg) {
        //response.setCharacterEncoding("UTF-8");
        //response.setContentType("application/json; charset=utf-8");
        result.setCode(code);
        result.setMsg(msg);
        String str_result = result.toJSONString();
        return str_result;
    }

    public  String sortString(String str) {
        String result = "";
        if (!CommonUtil.isEmpty(str)) {
            char[] arrayCh = str.toCharArray();
            Arrays.sort(arrayCh);
            result = String.valueOf(arrayCh);
        }
        return result;
    }

    public String getSign(String appId, String appKey, String appSecret, String data, String timestamp) {
        // 待加密
        String queryStr =appId+ appKey + appSecret + timestamp + data;
//        LogUtil.e(TAG, "queryStr>>==>>" + queryStr);
        String sortedStr = sortString(queryStr);
//        LogUtil.e(TAG, "sortedStr>>==>>" + sortedStr);
        String sha256edStr = SHA256Encrypt.bin2hex(sortedStr).toLowerCase();
//        LogUtil.e(TAG, "sha256edStr>>==>>" + sha256edStr);
//        String base64Str = Base64.encodeToString(sha256edStr.getBytes(), Base64.NO_WRAP);
//        String base64Str = StringUtils.replaceEnter(Base64.encodeToString(sha256edStr.getBytes(), Base64.NO_WRAP), "");
//        LogUtil.e(TAG, "加密后>>==>>" + base64Str);
        return sha256edStr;
    }


}
