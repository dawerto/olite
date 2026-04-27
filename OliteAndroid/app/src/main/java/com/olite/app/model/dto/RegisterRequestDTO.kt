package com.olite.app.model.dto

data class RegisterRequestDTO(
    val email: String,
    val password: String,
    val nombre: String,
    val apellidos: String
)
