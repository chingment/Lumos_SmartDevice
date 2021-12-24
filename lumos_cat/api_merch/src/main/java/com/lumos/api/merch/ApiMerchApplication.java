package com.lumos.api.merch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lumos.api.merch.mapper")
public class ApiMerchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMerchApplication.class, args);
    }

}
