package com.olite.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olite.app.model.dto.PedidoDTO
import com.olite.app.repository.PedidoRepository
import kotlinx.coroutines.launch

class PedidoViewModel : ViewModel() {

    private val repository = PedidoRepository()

    private val _pedidos = MutableLiveData<List<PedidoDTO>>()
    val pedidos: LiveData<List<PedidoDTO>> = _pedidos

    private val _pedidoRealizado = MutableLiveData<PedidoDTO?>()
    val pedidoRealizado: LiveData<PedidoDTO?> = _pedidoRealizado

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getPedidosUsuario(idUsuario: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPedidosUsuario(idUsuario, token)
                if (response.isSuccessful) {
                    _pedidos.value = response.body()
                } else {
                    _error.value = "Error al cargar pedidos"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun realizarPedido(idUsuario: Int, metodoPago: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.realizarPedido(idUsuario, metodoPago, token)
                if (response.isSuccessful) {
                    _pedidoRealizado.value = response.body()
                } else {
                    _error.value = "Error al realizar pedido"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun getTodosPedidos(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTodosPedidos(token)
                if (response.isSuccessful) {
                    _pedidos.value = response.body()
                } else {
                    _error.value = "Error al cargar pedidos"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun cambiarEstadoPedido(idPedido: Int, nuevoEstado: String, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.cambiarEstadoPedido(idPedido, nuevoEstado, token)
                if (response.isSuccessful) {
                    // Recargar la lista completa de pedidos (admin ve todos)
                    getTodosPedidos(token)
                } else {
                    _error.value = "Error al cambiar estado"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }
    fun resetPedidoRealizado() {
        _pedidoRealizado.value = null
    }
}