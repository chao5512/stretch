package com.deepthoughtdata.stretch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.deepthoughtdata.stretch")
@EnableEurekaClient
@RestController
public class AccountApplication {
	@RequestMapping("/hello")
	public String Hello(@RequestParam String name) {
		return "Hello "+name+"i am from port:" +port+"!";
	}

	@Value("${server.port}")
	String port;

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}
}
