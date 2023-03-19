package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableFeignClients(basePackages = {"com.xuecheng.content.feignclient"})
@SpringBootApplication
public class XuechengPlusContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XuechengPlusContentServiceApplication.class, args);
    }

}
