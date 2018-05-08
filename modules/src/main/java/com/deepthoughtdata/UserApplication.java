package com.deepthoughtdata;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new SpringApplicationBuilder(UserApplication.class).run(args);
    }
}
