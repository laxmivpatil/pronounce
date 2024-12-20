package com.abcm.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class JwtDemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(JwtDemoApplication.class, args);
	}
	
}
