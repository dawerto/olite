package com.olite.app.model.dto

data class ProductoDTO(
    val idProducto: Int = 0,
    val nombreProducto: String,
    val descripcion: String? = null,
    val precio: Double,
    val stock: Int,
    val imagen: String? = null
)