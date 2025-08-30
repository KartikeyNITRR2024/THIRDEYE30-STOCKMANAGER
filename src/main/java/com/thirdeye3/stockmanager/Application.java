package com.thirdeye3.stockmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling 
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
