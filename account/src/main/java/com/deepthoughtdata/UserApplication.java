package com.deepthoughtdata;

import com.deepthoughtdata.util.UploadConfigUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//springboot加载UploadUtil工具类
@Import({UploadConfigUtil.class})
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class UserApplication {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new SpringApplicationBuilder(UserApplication.class).run(args);
    }
}
