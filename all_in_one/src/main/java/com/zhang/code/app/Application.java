package com.zhang.code.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.zhang.code.redis.*"})
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
