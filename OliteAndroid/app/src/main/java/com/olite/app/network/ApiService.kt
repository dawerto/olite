package com.olite.app.network

import com.olite.app.model.dto.CarritoDTO
import com.olite.app.model.dto.LoginRequestDTO
import com.olite.app.model.dto.LoginResponseDTO
import com.olite.app.model.dto.PedidoDTO
import com.olite.app.model.dto.ProductoDTO
import com.olite.app.model.dto.RegisterRequestDTO
import com.olite.app.model.dto.UsuarioDTO
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // AUTH
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDTO): Response<Void>

    // PRODUCTOS
    @GET("api/productos")
    suspend fun getProductos(): Response<List<ProductoDTO>>

    @GET("api/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Int): Response<ProductoDTO>

    @GET("api/productos/buscar")
    suspend fun buscarProductos(@Query("nombre") nombre: String): Response<List<ProductoDTO>>

    // PRODUCTOS ADMIN
    @POST("api/productos")
    suspend fun crearProducto(
        @Body producto: ProductoDTO,
        @Header("Authorization") token: String
    ): Response<ProductoDTO>

    @PUT("api/productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Int,
        @Body producto: ProductoDTO,
        @Header("Authorization") token: String
    ): Response<ProductoDTO>

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<Void>

    // CARRITO
    @GET("api/carrito/{idUsuario}")
    suspend fun getCarrito(@Path("idUsuario") idUsuario: Int, @Header("Authorization") token: String): Response<CarritoDTO>

    @POST("api/carrito/{idUsuario}/productos/{idProducto}")
    suspend fun añadirAlCarrito(
        @Path("idUsuario") idUsuario: Int,
        @Path("idProducto") idProducto: Int,
        @Query("cantidad") cantidad: Int,
        @Header("Authorization") token: String
    ): Response<CarritoDTO>
    @DELETE("api/carrito/{idUsuario}/productos/{idProducto}")
    suspend fun eliminarDelCarrito(@Path("idUsuario") idUsuario: Int, @Path("idProducto") idProducto: Int, @Header("Authorization") token: String): Response<CarritoDTO>

    @DELETE("api/carrito/{idUsuario}/vaciar")
    suspend fun vaciarCarrito(@Path("idUsuario") idUsuario: Int, @Header("Authorization") token: String): Response<Void>

    // PEDIDOS
    @GET("api/pedidos/usuario/{idUsuario}")
    suspend fun getPedidosUsuario(@Path("idUsuario") idUsuario: Int, @Header("Authorization") token: String): Response<List<PedidoDTO>>

    @POST("api/pedidos/{idUsuario}")
    suspend fun realizarPedido(@Path("idUsuario") idUsuario: Int, @Query("metodoPago") metodoPago: String, @Header("Authorization") token: String): Response<PedidoDTO>

    // USUARIOS
    @GET("api/usuarios")
    suspend fun getUsuarios(@Header("Authorization") token: String): Response<List<UsuarioDTO>>

    @DELETE("api/usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int, @Header("Authorization") token: String): Response<Void>

    @GET("api/usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int, @Header("Authorization") token: String): Response<UsuarioDTO>

    @PUT("api/usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body usuario: UsuarioDTO, @Header("Authorization") token: String): Response<UsuarioDTO>

    // PEDIDOS ADMIN
    @GET("api/pedidos/todos")
    suspend fun getTodosPedidos(@Header("Authorization") token: String): Response<List<PedidoDTO>>

    @PUT("api/pedidos/estado/{idPedido}")
    suspend fun cambiarEstadoPedido(@Path("idPedido") idPedido: Int, @Query("nuevoEstado") nuevoEstado: String, @Header("Authorization") token: String): Response<PedidoDTO>
}