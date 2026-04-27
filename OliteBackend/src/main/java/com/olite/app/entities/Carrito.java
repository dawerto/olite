package com.olite.app.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Carrito")
@Data

public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Integer idCarrito;

    @OneToOne // 1 Usuario para 1 Carrito
    @JoinColumn(name = "id_usuario", unique = true, nullable = false)  // FK -> id_usuario de TABLA Usuario
    private Usuario usuario;

    @Column(name = "fecha_creacion", insertable = false, updatable = false) // Solo dejamos la fecha creación en la BD
    private LocalDateTime fechaCreacion;
}
