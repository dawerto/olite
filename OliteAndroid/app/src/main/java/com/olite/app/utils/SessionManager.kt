package com.olite.app.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("olite_prefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_TOKEN = "token"
        const val KEY_ROL = "rol"
        const val KEY_ID_USUARIO = "idUsuario"
        const val KEY_EMAIL = "email"
    }

    // Guardar Usuario despues del login
    fun guardarSesion(token: String, rol: String, idUsuario: Int, email: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_ROL, rol)
            .putInt(KEY_ID_USUARIO, idUsuario)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getRol(): String? = prefs.getString(KEY_ROL, null)

    fun getIdUsuario(): Int = prefs.getInt(KEY_ID_USUARIO, -1) // -1 id

    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    fun isLoggedIn(): Boolean = getToken() != null  // true si hay token guardado

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }
}