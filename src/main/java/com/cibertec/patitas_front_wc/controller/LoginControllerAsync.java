package com.cibertec.patitas_front_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.cibertec.patitas_front_wc.dto.LoginRequestDTO;
import com.cibertec.patitas_front_wc.dto.LoginResponseDTO;
import com.cibertec.patitas_front_wc.dto.LogoutRequestDTO;
import com.cibertec.patitas_front_wc.dto.LogoutResponseDTO;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginControllerAsync {

    @Autowired
    WebClient webClientAutenticacion;

    @PostMapping("/autenticar-async")
    public Mono<LoginResponseDTO> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {

        // validar campos de entrada
        if(loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().length() == 0 ||
            loginRequestDTO.numeroDocumento() == null || loginRequestDTO.numeroDocumento().trim().length() == 0 ||
            loginRequestDTO.password() == null || loginRequestDTO.password().trim().length() == 0) {

            return Mono.just(new LoginResponseDTO("01", "Error: Debe completar correctamente sus credenciales", "", ""));
        }
        try {
            // consumir servicio de autenticación (Del Backend)
            return webClientAutenticacion.post()
                    .uri("/login")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class)
                    .flatMap(response -> {

                        if(response.codigo().equals("00")) {
                            return Mono.just(new LoginResponseDTO("00", "", response.usuario(), ""));
                        } else {
                            return Mono.just(new LoginResponseDTO("02", "Error: Autenticación fallida", "", ""));
                        }

                    });

        } catch(Exception e) {

            System.out.println(e.getMessage());
            return Mono.just(new LoginResponseDTO("99", "Error: Ocurrió un problema en la autenticación", "", ""));

        }
    }
    @PostMapping("/logout-async")
    public Mono<ResponseEntity<LogoutResponseDTO>> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        try{
        return webClientAutenticacion.post()
                .uri("/logout")
                .body(Mono.just(logoutRequestDTO), LogoutRequestDTO.class)
                .retrieve()
                .bodyToMono(LogoutResponseDTO.class)
                .map(logoutResponseDTO -> {
                    if (logoutResponseDTO.resultado()) {
                        return ResponseEntity.ok(new LogoutResponseDTO(true, logoutResponseDTO.fecha(), "Sesión cerrada exitosamente"));
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new LogoutResponseDTO(false, null, "Ocurrió un error al cerrar la sesión"));
                    }
                });
        }catch(Exception e){

            System.out.println("Error inesperado: " + e.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LogoutResponseDTO(false, null, "Error inesperado: No se pudo realizar el cerrado de sesión")));
        }
    }

}
