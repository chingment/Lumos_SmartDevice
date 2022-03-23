package com.caterbao.lumos.api.merch;

import com.caterbao.lumos.api.merch.handler.AppAuthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private AppAuthHandler appAuthHandler;

    //构造方法
    public WebConfiguration(AppAuthHandler appAuthHandler){
        this.appAuthHandler = appAuthHandler;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedHeaders("*")
//                .allowedMethods("*")
//                .allowedOrigins("*");
    }

    @Autowired
    private Environment env;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");


        String staticPath = env.getProperty("lumos.custom.save-file-path")+"/upload/";

        registry.addResourceHandler("/static/upload/**").addResourceLocations("file:"+staticPath);
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+staticPath);

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer){

        configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3)));
        configurer.setDefaultTimeout(30000);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        List<String> excludePath = new ArrayList<>();
        //排除拦截，除了注册登录(此时还没token)，其他都拦截
        excludePath.add("/own/loginByAccount");     //登录
        excludePath.add("/own/logout");     //登录
        excludePath.add("/static/**");  //静态资源
        excludePath.add("/upload/**");
        excludePath.add("/error");
        excludePath.add("/assets/**");  //静态资源

        registry.addInterceptor(appAuthHandler)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
