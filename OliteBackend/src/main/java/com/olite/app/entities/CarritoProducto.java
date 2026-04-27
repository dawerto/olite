package com.olite.app.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Carrito_producto")
@Data

public class CarritoProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito_producto")
    private Integer idCarritoProducto;

    @ManyToOne
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unidad", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal precioUnidad;
}
