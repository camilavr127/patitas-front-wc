package com.cibertec.patitas_front_wc.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.netty.http.client.HttpClient;

public class WebClientConfig {
    @Bean
    public WebClient webClientAutentication(WebClient.Builder builder) {
        HttpClient httpClient = HttpClient.create()
        //El tiempo que el cliente va a esperar para conectarse al servidor
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
        //El tiempo qde lectura de toda la respuesta
        .responseTimeout(Duration.ofSeconds(10))
        //El tiempo que el servidor va a esperar entre paquetes
        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)));       
    
        return builder
        .baseUrl("http://localhost:8081/auth")
        //Conecta con el cliente http reactivo
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
    }

}