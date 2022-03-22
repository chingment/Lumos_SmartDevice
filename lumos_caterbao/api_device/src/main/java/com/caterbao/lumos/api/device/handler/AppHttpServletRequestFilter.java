package com.caterbao.lumos.api.device.handler;

import com.caterbao.lumos.locals.common.web.RequestReaderHttpServletRequestWrapper;
import com.caterbao.lumos.locals.common.web.ResponseReaderHttpServletResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@WebFilter(urlPatterns = "/*", filterName = "LogFilter")
public class AppHttpServletRequestFilter implements Filter {
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

//        RequestReaderHttpServletRequestWrapper requestWrapper = new RequestReaderHttpServletRequestWrapper((HttpServletRequest) request);
//        RespReaderHttpServletResponseWrapper responseWrapper=new RespReaderHttpServletResponseWrapper((HttpServletResponse) response);
//        chain.doFilter(requestWrapper, responseWrapper);


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