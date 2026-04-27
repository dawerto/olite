package com.olite.app.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {
    private Integer idPedido;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private String estadoPedido;
    private String metodoPago;
    private List<PedidoDetalleDTO> detalles;
}
