package com.cibertec.patitas_front_wc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cibertec.patitas_front_wc.config.FeignClientConfig;
import com.cibertec.patitas_front_wc.dto.LogoutRequestDTO;
import com.cibertec.patitas_front_wc.dto.LogoutResponseDTO;

@FeignClient(name= "autenticacion", url ="http://localhost:8081/auth", configuration = FeignClientConfig.class)
public interface AuthClient {
    @PostMapping("/logout")
    ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO);

}
