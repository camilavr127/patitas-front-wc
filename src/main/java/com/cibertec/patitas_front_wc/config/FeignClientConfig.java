package com.cibertec.patitas_front_wc.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Request;

@Configuration
@EnableFeignClients
public class FeignClientConfig {
    @SuppressWarnings("deprecation")
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(10000, 10000); // Timeouts in milliseconds
    }
}
