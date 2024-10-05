package com.cibertec.patitas_front_wc.dto;
/*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) //Contraseña
 */
public record LoginResponseDTO(String codigo, String mensaje, String usuario, String correo)  {

}
