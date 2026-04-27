package com.olite.app.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Producto")
@Data

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @Column(name = "descripcion", columnDefinition = "TEXT") // Texto largo
    private String descripcion;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2) // Usamos precision y scale para no comernos decimales
    private java.math.BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "imagen", length = 255)
    private String imagen;
}
