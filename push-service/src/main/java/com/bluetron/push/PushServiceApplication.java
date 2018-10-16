package com.bluetron.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
//@EnableDiscoveryClient
public class PushServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PushServiceApplication.class, args);
	}
}
