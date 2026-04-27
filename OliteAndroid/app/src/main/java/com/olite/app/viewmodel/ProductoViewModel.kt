package com.olite.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olite.app.model.dto.ProductoDTO
import com.olite.app.repository.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    private val repository = ProductoRepository()

    private val _productos = MutableLiveData<List<ProductoDTO>>()
    val productos: LiveData<List<ProductoDTO>> = _productos

    private val _productoDetalle = MutableLiveData<ProductoDTO?>()
    val productoDetalle: LiveData<ProductoDTO?> = _productoDetalle

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> = _operacionExitosa

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getProductos() {
        viewModelScope.launch {
            try {
                val response = repository.getProductos()
                if (response.isSuccessful) {
                    _productos.value = response.body()
                } else {
                    _error.value = "Error al cargar productos"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun getProductoById(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProductoById(id)
                if (response.isSuccessful) {
                    _productoDetalle.value = response.body()
                } else {
                    _error.value = "Producto no encontrado"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun buscarProductos(nombre: String) {
        viewModelScope.launch {
            try {
                val response = repository.buscarProductos(nombre)
                if (response.isSuccessful) {
                    _productos.value = response.body()
                } else {
                    _error.value = "Error al buscar productos"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun crearProducto(producto: ProductoDTO, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.crearProducto(producto, token)
                if (response.isSuccessful) {
                    _operacionExitosa.value = true
                } else {
                    _error.value = "Error al crear producto"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun actualizarProducto(id: Int, producto: ProductoDTO, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarProducto(id, producto, token)
                if (response.isSuccessful) {
                    _operacionExitosa.value = true
                } else {
                    _error.value = "Error al actualizar producto"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun eliminarProducto(id: Int, token: String) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarProducto(id, token)
                if (response.isSuccessful) {
                    _operacionExitosa.value = true
                } else {
                    _error.value = "Error al eliminar producto"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión con el servidor"
            }
        }
    }

    fun resetOperacion() {
        _operacionExitosa.value = false
    }
}