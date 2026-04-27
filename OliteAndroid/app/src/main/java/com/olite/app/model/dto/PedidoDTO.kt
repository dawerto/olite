package com.olite.app.model.dto

data class PedidoDTO(
    val idPedido: Int,
    val fechaPedido: String,
    val total: Double,
    val estadoPedido: String,
    val metodoPago: String,
    val detalles: List<PedidoDetalleDTO>
)