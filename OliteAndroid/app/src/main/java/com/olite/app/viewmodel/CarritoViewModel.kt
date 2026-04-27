package com.olite.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olite.app.model.dto.CarritoDTO
import com.olite.app.repository.CarritoRepository
import kotlinx.coroutines.launch

class CarritoViewModel : ViewModel() {

    private val repository = CarritoRepository()

    private val _carrito = MutableLiveData<CarritoDTO?>()
    val carrito: LiveData<CarritoDTO?> = _carrito

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> = _operacionExitosa

    fun getCarrito(idUsuario: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCarrito(idUsuario, token)
                if (response.isSuccessful) {
                    _carrito.value = response.body()
                } else {
                    _error.value = "Error al cargar carrito"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun añadirAlCarrito(idUsuario: Int, idProducto: Int, cantidad: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.añadirAlCarrito(idUsuario, idProducto, cantidad, token)
                if (response.isSuccessful) {
                    _carrito.value = response.body()
                    _operacionExitosa.value = true
                } else {
                    _error.value = "Error al añadir producto"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun eliminarDelCarrito(idUsuario: Int, idProducto: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarDelCarrito(idUsuario, idProducto, token)
                if (response.isSuccessful) {
                    _carrito.value = response.body()
                } else {
                    _error.value = "Error al eliminar producto"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun vaciarCarrito(idUsuario: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.vaciarCarrito(idUsuario, token)
                if (response.isSuccessful) {
                    _operacionExitosa.value = true
                } else {
                    _error.value = "Error al vaciar carrito"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }
}