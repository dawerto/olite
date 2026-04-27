package com.olite.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class CarritoDTO {
    private Integer idCarrito;
    private Integer idUsuario;
    private List<CarritoProductoDTO> productos;
}
