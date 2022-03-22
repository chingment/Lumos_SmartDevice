package com.caterbao.lumos.api.merch.handler;

import com.caterbao.lumos.locals.common.web.RequestReaderHttpServletRequestWrapper;
import com.caterbao.lumos.locals.common.web.ResponseReaderHttpServletResponseWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AppHttpServletRequestFilter implements Filter {
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {


        HttpServletResponse l_response = (HttpServletResponse) response;

        l_response.setHeader("Access-Control-Allow-Origin","*");
        l_response.setHeader("Access-Control-Allow-Credentials", "true");
        l_response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        l_response.setHeader("Access-Control-Max-Age", "5000");
        l_response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,Authorization,Token");

        ServletRequest requestWrapper = null;
        ResponseReaderHttpServletResponseWrapper responseWrapper=new ResponseReaderHttpServletResponseWrapper((HttpServletResponse) response);
        if(request instanceof HttpServletRequest) {
            String contentType = request.getContentType();
            if(contentType!=null) {
                if (!contentType.contains("multipart/form-data")) {
                    requestWrapper = new RequestReaderHttpServletRequestWrapper((HttpServletRequest) request);
                }
            }
        }

        if(requestWrapper == null) {
            chain.doFilter(request, responseWrapper);
        } else {
            chain.doFilter(requestWrapper, responseWrapper);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
}