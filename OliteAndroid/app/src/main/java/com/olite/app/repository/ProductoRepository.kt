package com.olite.app.repository

import com.olite.app.model.dto.ProductoDTO
import com.olite.app.network.RetrofitClient
import retrofit2.Response

class ProductoRepository {

    private val api = RetrofitClient.instance

    suspend fun getProductos(): Response<List<ProductoDTO>> {
        return api.getProductos()
    }

    suspend fun getProductoById(id: Int): Response<ProductoDTO> {
        return api.getProductoById(id)
    }

    suspend fun buscarProductos(nombre: String): Response<List<ProductoDTO>> {
        return api.buscarProductos(nombre)
    }

    suspend fun crearProducto(producto: ProductoDTO, token: String): Response<ProductoDTO> {
        return api.crearProducto(producto, "Bearer $token")
    }

    suspend fun actualizarProducto(id: Int, producto: ProductoDTO, token: String): Response<ProductoDTO> {
        return api.actualizarProducto(id, producto, "Bearer $token")
    }

    suspend fun eliminarProducto(id: Int, token: String): Response<Void> {
        return api.eliminarProducto(id, "Bearer $token")
    }
}