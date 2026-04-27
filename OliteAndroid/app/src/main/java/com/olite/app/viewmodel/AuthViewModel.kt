package com.olite.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olite.app.model.dto.LoginResponseDTO
import com.olite.app.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginResult = MutableLiveData<LoginResponseDTO?>()
    val loginResult: LiveData<LoginResponseDTO?> = _loginResult

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                if (response.isSuccessful) {
                    _loginResult.value = response.body()
                } else {
                    _error.value = "Los datos introducidos son incorrectos"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun register(email: String, password: String, nombre: String, apellidos: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(email, password, nombre, apellidos)
                if (response.isSuccessful) {
                    _registerResult.value = true
                } else {
                    _error.value = "Error al registrarse"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }
}