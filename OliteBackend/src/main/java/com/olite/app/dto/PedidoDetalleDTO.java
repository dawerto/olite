package com.olite.app.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PedidoDetalleDTO {
    private Integer idPedidoDetalle;
    private Integer idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioHistorico;
    private String estado;
}
