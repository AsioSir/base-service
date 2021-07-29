package com.asio.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//扫描mybatis生成的mapper文件
//@MapperScan({"com.asio.tools.*"})
public class BaseToolsApplication {
    public static void main(String[] args) {
        //启动程序
        SpringApplication.run(BaseToolsApplication.class, args);
    }
}
