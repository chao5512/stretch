package com.deepthoughtdata;

import com.deepthoughtdata.config.AccessTokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;


/**
 * ZuulApplication
 *
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
//@EnableResourceServer
public class ZuulApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ZuulApplication.class, args);
    }

    @Bean
    public AccessTokenFilter accessTokenFilter(){
        return new AccessTokenFilter();
    }

}

