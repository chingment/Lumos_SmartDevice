package com.caterbao.lumos.api.device.handler;

import com.caterbao.lumos.locals.common.CommonUtil;
import com.caterbao.lumos.locals.common.CustomResult;
import com.caterbao.lumos.locals.common.SHA256Encrypt;
import com.caterbao.lumos.locals.dal.mapper.AppSoftMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

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
        logger.debug("AppAuthHandler.preHandle");

        this.response=response;

       String contentType= request.getContentType();
       String method=request.getMethod();
        if (method.equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String appId = request.getHeader("appId");
        String appKey = request.getHeader("appKey");
        String sign = request.getHeader("sign");
        String timestamp = request.getHeader("timestamp");

        if (appId == null || appKey == null || sign == null || timestamp == null) {
            String str_result = getResponse(2501,"缺少必要参数");
            response.getWriter().append(str_result);
            return false;
        }

        String appSecret = appSoftMapper.getSecretByKey(appKey);

        if (appSecret == null) {
            String str_result = getResponse(2501,"应用无效");
            response.getWriter().append(str_result);
            return false;
        }

        String data = HttpHelper.getBodyString(request);

        String my_sign = getSign(appId, appKey, appSecret, data, timestamp);

        if(!sign.equals(my_sign)) {
            String str_result = getResponse(2501,"签名错误");
            response.getWriter().append(str_result);
            return false;
        }

        return true;
    }

    private String getResponse(int code,String msg) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
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
