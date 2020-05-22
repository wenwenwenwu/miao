package com.lin.missyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.lin")
public class MiaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoApplication.class, args);
    }

}
