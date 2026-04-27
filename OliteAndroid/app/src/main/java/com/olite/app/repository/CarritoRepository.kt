package com.olite.app.repository

import com.olite.app.model.dto.CarritoDTO
import com.olite.app.network.RetrofitClient
import retrofit2.Response

class CarritoRepository {

    private val api = RetrofitClient.instance

    suspend fun getCarrito(idUsuario: Int, token: String): Response<CarritoDTO> {
        return api.getCarrito(idUsuario, "Bearer $token")
    }

    suspend fun añadirAlCarrito(idUsuario: Int, idProducto: Int, cantidad: Int, token: String): Response<CarritoDTO> {
        return api.añadirAlCarrito(idUsuario, idProducto, cantidad, "Bearer $token")
    }

    suspend fun eliminarDelCarrito(idUsuario: Int, idProducto: Int, token: String): Response<CarritoDTO> {
        return api.eliminarDelCarrito(idUsuario, idProducto, "Bearer $token")
    }

    suspend fun vaciarCarrito(idUsuario: Int, token: String): Response<Void> {
        return api.vaciarCarrito(idUsuario, "Bearer $token")
    }
}