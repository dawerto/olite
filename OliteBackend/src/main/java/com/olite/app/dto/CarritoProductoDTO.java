package com.olite.app.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CarritoProductoDTO {
    private Integer idCarritoProducto;
    private Integer idProducto;
    private String nombreProducto;
    private String imagen;
    private Integer cantidad;
    private BigDecimal precioUnidad;
}
