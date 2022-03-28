package com.caterbao.lumos.api.device;

import com.caterbao.lumos.api.device.handler.AppHttpServletRequestFilter;
import com.caterbao.lumos.locals.common.CommonUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.Timestamp;

@SpringBootApplication(scanBasePackages = "com.caterbao.lumos")
@MapperScan("com.caterbao.lumos.locals.dal.mapper")
@EnableTransactionManagement
public class ApiDeviceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ApiDeviceApplication.class, args);

    }

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AppHttpServletRequestFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("appHttpServletRequestFilter");
        registration.setOrder(1);
        return registration;
    }
}
