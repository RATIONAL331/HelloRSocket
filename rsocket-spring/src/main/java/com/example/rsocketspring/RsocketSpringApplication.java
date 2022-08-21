package com.example.rsocketspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RsocketSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsocketSpringApplication.class, args);
	}

}
