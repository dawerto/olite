package com.olite.app.model.dto

data class PedidoDetalleDTO(
    val idPedidoDetalle: Int,
    val idProducto: Int,
    val nombreProducto: String,
    val cantidad: Int,
    val precioHistorico: Double,
    val estado: String? = null
)