package com.olite.app.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Integer idProducto;
    private String nombreProducto;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagen;
}
