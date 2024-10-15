package com.cibertec.patitas_front_wc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PatitasFrontWcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatitasFrontWcApplication.class, args);
	}

}
