package com.sav.authentification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class AuthentificationApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(AuthentificationApplication.class, args);
	}


}
