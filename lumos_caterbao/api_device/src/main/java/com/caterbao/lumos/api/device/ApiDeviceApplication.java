package com.caterbao.lumos.api.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.caterbao.lumos")
@MapperScan("com.caterbao.lumos.locals.dal.mapper")
public class ApiDeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiDeviceApplication.class, args);
    }

}
