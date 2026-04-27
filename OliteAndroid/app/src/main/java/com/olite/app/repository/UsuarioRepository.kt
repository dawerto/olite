package com.olite.app.repository

import com.olite.app.model.dto.UsuarioDTO
import com.olite.app.network.RetrofitClient
import retrofit2.Response

class UsuarioRepository {

    private val api = RetrofitClient.instance

    suspend fun getUsuarios(token: String): Response<List<UsuarioDTO>> {
        return api.getUsuarios("Bearer $token")
    }

    suspend fun getUsuarioById(id: Int, token: String): Response<UsuarioDTO> {
        return api.getUsuarioById(id, "Bearer $token")
    }

    suspend fun actualizarUsuario(id: Int, usuario: UsuarioDTO, token: String): Response<UsuarioDTO> {
        return api.actualizarUsuario(id, usuario, "Bearer $token")
    }

    suspend fun eliminarUsuario(id: Int, token: String): Response<Void> {
        return api.eliminarUsuario(id, "Bearer $token")
    }
}