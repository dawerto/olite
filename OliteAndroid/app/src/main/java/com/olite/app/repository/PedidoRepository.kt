package com.olite.app.repository

import com.olite.app.model.dto.PedidoDTO
import com.olite.app.network.RetrofitClient
import retrofit2.Response

class PedidoRepository {

    private val api = RetrofitClient.instance

    suspend fun getPedidosUsuario(idUsuario: Int, token: String): Response<List<PedidoDTO>> {
        return api.getPedidosUsuario(idUsuario, "Bearer $token")
    }

    suspend fun realizarPedido(idUsuario: Int, metodoPago: String, token: String): Response<PedidoDTO> {
        return api.realizarPedido(idUsuario, metodoPago, "Bearer $token")
    }

    suspend fun getTodosPedidos(token: String): Response<List<PedidoDTO>> {
        return api.getTodosPedidos("Bearer $token")
    }

    suspend fun cambiarEstadoPedido(idPedido: Int, nuevoEstado: String, token: String): Response<PedidoDTO> {
        return api.cambiarEstadoPedido(idPedido, nuevoEstado, "Bearer $token")
    }
}