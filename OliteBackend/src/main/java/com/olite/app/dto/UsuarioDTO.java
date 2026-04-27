package com.olite.app.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String email;
    private String rol;
}
