package com.olite.app.model.dto

data class CarritoDTO(
    val idCarrito: Int,
    val idUsuario: Int,
    val productos: List<CarritoProductoDTO>
)
