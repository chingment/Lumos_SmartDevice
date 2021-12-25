package com.caterbao.lumos.api.merch.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AppAuthHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        System.out.println("AppAuthHandler.preHandle");

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        return true;
//        response.setCharacterEncoding("utf-8");
//        String token = request.getHeader("token");
//        if(token != null){
//            boolean result = TokenUtil.verify(token);
//            if(result){
//                System.out.println("通过拦截器");
//                return true;
//            }
//        }
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        try{
//            JSONObject json = new JSONObject();
//            json.put("msg","token verify fail");
//            json.put("code","50000");
//            response.getWriter().append(json.toJSONString());
//            System.out.println("认证失败，未通过拦截器");
//        }catch (Exception e){
//            e.printStackTrace();
//            response.sendError(500);
//            return false;
//        }
//        return false;
    }
}
