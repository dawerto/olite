package com.olite.app.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Usuario")
@Data // Getters/Setters, toString() y Constructores -> Lombok

public class Usuario {
    @Id // ->PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellidos", length = 100)
    private String apellidos;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "email", nullable = false, unique = true, length = 100) // 1 correo para 1 usuario
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol = "ROLE_USER";  // ROL MIEMBRO default

    @Column(name = "fecha_registro", insertable = false, updatable = false) // ignoramos INSERT desde java y no actualiza fecha. solo desde la BD
    private LocalDateTime fechaRegistro;
}
