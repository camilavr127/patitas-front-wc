package com.cibertec.patitas_front_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.cibertec.patitas_front_wc.dto.LoginRequestDTO;
import com.cibertec.patitas_front_wc.dto.LoginResponseDTO;
import com.cibertec.patitas_front_wc.view.LoginModel;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/login")
@CrossOrigin(origins = "")
public class LoginController {
    @Autowired
    WebClient clientAuth;

    @GetMapping("/inicio")
    public String inicio(Model model){
        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }
    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento, 
                            @RequestParam("numeroDocumento") String numeroDocumento, 
                            @RequestParam("password") String password,
                            Model model){
                                
        // Validaciones simples para el ejemplo
        if(tipoDocumento == null || tipoDocumento.trim().length()==0 ||
        numeroDocumento == null || numeroDocumento.trim().length()==0 ||
        password == null || password.trim().length()==0){
            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);

            return "inicio";
        } 
        try {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);
            Mono<LoginResponseDTO> monoLoginResponse = clientAuth.post()
            .uri("/login")
            .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
            .retrieve()
            .bodyToMono(LoginResponseDTO.class);

            //Recuperar resultado del mono (sincrono o bloqueante)
            LoginResponseDTO loginResponseDTO = monoLoginResponse.block();

            if (loginResponseDTO.codigo().equals("00")){
            LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.usuario());
            model.addAttribute("loginModel", loginModel);
            return "principal";
            } else{
                LoginModel loginModel = new LoginModel("02", "Error: Autenticacion fallida", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";
            }
        }catch (Exception ex){
            LoginModel loginModel = new LoginModel("99", "Error: No se pudo conectar con el sistema de autenticacion", "");
            model.addAttribute("loginModel", loginModel);
            System.out.println(ex.getMessage());
            return "inicio";}
        }
    }

    

