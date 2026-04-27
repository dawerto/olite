package com.olite.app.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Pedido_detalle")
@Data

public class PedidoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_detalle")
    private Integer idPedidoDetalle;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "precio_historico_venta", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal precioHistorico;
}
