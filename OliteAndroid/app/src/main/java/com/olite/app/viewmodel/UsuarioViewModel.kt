package com.olite.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olite.app.model.dto.UsuarioDTO
import com.olite.app.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {

    private val repository = UsuarioRepository()

    private val _usuarios = MutableLiveData<List<UsuarioDTO>>()
    val usuarios: LiveData<List<UsuarioDTO>> = _usuarios

    private val _usuarioActual = MutableLiveData<UsuarioDTO?>()
    val usuarioActual: LiveData<UsuarioDTO?> = _usuarioActual

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> = _operacionExitosa

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getUsuarios(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUsuarios(token)
                if (response.isSuccessful) _usuarios.value = response.body()
                else _error.value = "Error al cargar usuarios"
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun getUsuarioById(id: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUsuarioById(id, token)
                if (response.isSuccessful) _usuarioActual.value = response.body()
                else _error.value = "Error al cargar el perfil"
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun actualizarUsuario(id: Int, usuario: UsuarioDTO, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarUsuario(id, usuario, token)
                if (response.isSuccessful) {
                    _usuarioActual.value = response.body()
                    _operacionExitosa.value = true
                } else {
                    _error.value = "Error al actualizar perfil"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun eliminarUsuario(id: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarUsuario(id, token)
                if (response.isSuccessful) _operacionExitosa.value = true
                else _error.value = "Error al eliminar usuario"
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }
}