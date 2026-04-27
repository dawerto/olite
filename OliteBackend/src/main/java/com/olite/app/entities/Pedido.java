package com.olite.app.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Entity
@Table(name = "Pedido")
@Data

public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Generated(event = EventType.INSERT)
    @Column(name = "fecha_pedido", insertable = false, updatable = false)
    private LocalDateTime fechaPedido;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal total;

    @Column(name = "estado_pedido", length = 30)
    private String estadoPedido = "PENDIENTE";

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;
}