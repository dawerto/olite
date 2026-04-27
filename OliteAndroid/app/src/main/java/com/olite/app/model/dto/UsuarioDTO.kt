package com.olite.app.model.dto

data class UsuarioDTO(
    val idUsuario: Int? = null,
    val nombre: String? = null,
    val apellidos: String? = null,
    val direccion: String? = null,
    val telefono: String? = null,
    val email: String? = null,
    val rol: String? = null
)
