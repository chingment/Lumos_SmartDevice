package com.caterbao.lumos.api.merch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Component
public class BaseController {

    @Autowired
    private HttpServletRequest request;

    public String getCurrentUserId() {

        //根据redis缓存获取用户Id
        if (request != null) {
            String token = request.getHeader("Token");
            System.out.println(token);
        }

        return null;
    }

}
