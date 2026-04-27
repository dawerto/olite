package com.olite.app.services;

import com.olite.app.dto.CarritoDTO;
import com.olite.app.dto.CarritoProductoDTO;
import com.olite.app.entities.Carrito;
import com.olite.app.entities.CarritoProducto;
import com.olite.app.entities.Producto;
import com.olite.app.repositories.CarritoProductoRepository;
import com.olite.app.repositories.CarritoRepository;
import com.olite.app.repositories.ProductoRepository;
import com.olite.app.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final CarritoProductoRepository carritoProductoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    // Obtener carrito del usuario
    public CarritoDTO obtenerCarrito(Integer idUsuario) {
        Carrito carrito = carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el usuario: " + idUsuario));
        return convertirADTO(carrito);
    }

    // Añadir producto al carrito
    public CarritoDTO añadirProducto(Integer idUsuario, Integer idProducto, Integer cantidad) {
        Carrito carrito = carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el usuario: " + idUsuario));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + idProducto));

        // Comprobar si el producto ya está en el carrito
        CarritoProducto carritoProducto = carritoProductoRepository
                .findByCarrito_IdCarritoAndProducto_IdProducto(carrito.getIdCarrito(), idProducto)
                .orElse(null);

        if (carritoProducto != null) {
            // Si ya existe, aumentar cantidad
            carritoProducto.setCantidad(carritoProducto.getCantidad() + cantidad);
        } else {
            // Si no existe, crear nuevo
            carritoProducto = new CarritoProducto();
            carritoProducto.setCarrito(carrito);
            carritoProducto.setProducto(producto);
            carritoProducto.setCantidad(cantidad);
            carritoProducto.setPrecioUnidad(producto.getPrecio());
        }

        carritoProductoRepository.save(carritoProducto);
        return convertirADTO(carrito);
    }

    // Eliminar producto del carrito
    public CarritoDTO eliminarProducto(Integer idUsuario, Integer idProducto) {
        Carrito carrito = carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el usuario: " + idUsuario));

        CarritoProducto carritoProducto = carritoProductoRepository
                .findByCarrito_IdCarritoAndProducto_IdProducto(carrito.getIdCarrito(), idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en el carrito"));

        carritoProductoRepository.delete(carritoProducto);
        return convertirADTO(carrito);
    }

    // Vaciar carrito
    public void vaciarCarrito(Integer idUsuario) {
        Carrito carrito = carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el usuario: " + idUsuario));

        List<CarritoProducto> productos = carritoProductoRepository
                .findByCarrito_IdCarrito(carrito.getIdCarrito());

        carritoProductoRepository.deleteAll(productos);
    }

    // Convertir Entity a DTO
    private CarritoDTO convertirADTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setIdCarrito(carrito.getIdCarrito());
        dto.setIdUsuario(carrito.getUsuario().getIdUsuario());

        List<CarritoProducto> productos = carritoProductoRepository
                .findByCarrito_IdCarrito(carrito.getIdCarrito());

        dto.setProductos(productos.stream()
                .map(this::convertirProductoADTO)
                .collect(Collectors.toList()));

        return dto;
    }

    // Convertir CarritoProducto Entity -> DTO
    private CarritoProductoDTO convertirProductoADTO(CarritoProducto cp) {
        CarritoProductoDTO dto = new CarritoProductoDTO();
        dto.setIdCarritoProducto(cp.getIdCarritoProducto());
        dto.setIdProducto(cp.getProducto().getIdProducto());
        dto.setNombreProducto(cp.getProducto().getNombreProducto());
        dto.setImagen(cp.getProducto().getImagen());
        dto.setCantidad(cp.getCantidad());
        dto.setPrecioUnidad(cp.getPrecioUnidad());
        return dto;
    }

}
