package com.deepthoughtdata.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableAuthorizationServer
public class AuthserverApplication extends WebMvcConfigurerAdapter  {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthserverApplication.class);

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //SpringApplication.run(Application.class, args);
        new SpringApplicationBuilder(AuthserverApplication.class).web(true).run(args);
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }


}
