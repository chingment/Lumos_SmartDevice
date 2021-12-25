package com.caterbao.lumos.api.merch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("com.caterbao.lumos.locals.dal.mapper")
public class ApiMerchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMerchApplication.class, args);
    }

}
