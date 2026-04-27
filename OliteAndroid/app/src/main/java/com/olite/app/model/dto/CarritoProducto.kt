package com.olite.app.model.dto

data class CarritoProductoDTO(
    val idCarritoProducto: Int,
    val idProducto: Int,
    val nombreProducto: String,
    val imagen: String? = null,
    val cantidad: Int,
    val precioUnidad: Double
)