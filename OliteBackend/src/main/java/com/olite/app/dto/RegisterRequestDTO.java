package com.olite.app.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String nombre;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String email;
    private String password;
}
