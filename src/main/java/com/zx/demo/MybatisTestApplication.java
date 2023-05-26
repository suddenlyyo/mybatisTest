package com.zx.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: mybatisTest
 * @description:
 * @author: zhou  xun
 * @create: 2023-05-15 12:46
 */
@SpringBootApplication
@EnableTransactionManagement  //开启事务
public class MybatisTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisTestApplication.class, args);
    }

}
