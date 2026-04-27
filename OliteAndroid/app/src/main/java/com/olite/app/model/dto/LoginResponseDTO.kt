package com.olite.app.model.dto

data class LoginResponseDTO(
    val token: String,
    val email: String,
    val rol: String,
    val idUsuario: Int
)
