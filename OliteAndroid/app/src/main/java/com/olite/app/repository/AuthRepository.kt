package com.olite.app.repository

import com.olite.app.model.dto.LoginRequestDTO
import com.olite.app.model.dto.LoginResponseDTO
import com.olite.app.model.dto.RegisterRequestDTO
import com.olite.app.network.RetrofitClient
import retrofit2.Response

class AuthRepository {

    private val api = RetrofitClient.instance

    suspend fun login(email: String, password: String): Response<LoginResponseDTO> {
        return api.login(LoginRequestDTO(email, password))
    }

    suspend fun register(email: String, password: String, nombre: String, apellidos: String): Response<Void> {
        return api.register(RegisterRequestDTO(email, password, nombre, apellidos))
    }

    }
