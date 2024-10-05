package com.cibertec.patitas_front_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.cibertec.patitas_front_wc.dto.LoginRequestDTO;
import com.cibertec.patitas_front_wc.dto.LoginResponseDTO;
import com.cibertec.patitas_front_wc.view.LoginModel;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    RestTemplate restTemplate;

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
            // Simulacion de llamada a la API de autenticacion
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);
            LoginResponseDTO loginResponseDTO = restTemplate.postForObject("/login", loginRequestDTO, LoginResponseDTO.class);

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

