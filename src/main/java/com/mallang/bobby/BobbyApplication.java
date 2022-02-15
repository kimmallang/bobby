package com.mallang.bobby;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BobbyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BobbyApplication.class, args);
	}

}
