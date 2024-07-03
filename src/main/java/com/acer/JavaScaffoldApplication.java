package com.acer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 超哥
 */
@SpringBootApplication
@MapperScan("com.acer.dao")
public class JavaScaffoldApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaScaffoldApplication.class, args);
    }

}
