package com.asio.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//扫描mybatis生成的mapper文件
//@MapperScan({"com.asio.swagger.business.generator.*"})
public class BaseSwaggerApplication {
    public static void main(String[] args) {
        //启动程序
        SpringApplication.run(BaseSwaggerApplication.class, args);
    }
}
