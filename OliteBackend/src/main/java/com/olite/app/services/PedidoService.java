package com.olite.app.services;

import com.olite.app.dto.PedidoDTO;
import com.olite.app.dto.PedidoDetalleDTO;
import com.olite.app.entities.*;
import com.olite.app.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final PedidoDetalleRepository pedidoDetalleRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoProductoRepository carritoProductoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    // Obtener todos los pedidos (solo ADMIN)
    public List<PedidoDTO> obtenerTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener pedidos del usuario
    public List<PedidoDTO> obtenerPedidosUsuario(Integer idUsuario) {
        return pedidoRepository.findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener pedido por ID
    public PedidoDTO obtenerPorId(Integer idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
        return convertirADTO(pedido);
    }

    // Realizar pedido desde el carrito
    public PedidoDTO realizarPedido(Integer idUsuario, String metodoPago) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));

        Carrito carrito = carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el usuario: " + idUsuario));

        List<CarritoProducto> productosCarrito = carritoProductoRepository
                .findByCarrito_IdCarrito(carrito.getIdCarrito());

        if (productosCarrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Calcular total
        BigDecimal total = productosCarrito.stream()
                .map(cp -> cp.getPrecioUnidad().multiply(BigDecimal.valueOf(cp.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTotal(total);
        pedido.setMetodoPago(metodoPago);
        pedido.setEstadoPedido("PENDIENTE");
        pedidoRepository.save(pedido);

        // Crear detalles del pedido
        for (CarritoProducto cp : productosCarrito) {

            // Comprobar y descontar stock
            Producto producto = cp.getProducto();
            if (producto.getStock() < cp.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombreProducto());
            }
            producto.setStock(producto.getStock() - cp.getCantidad());
            productoRepository.save(producto);

            // Crear detalle
            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setPedido(pedido);
            detalle.setProducto(cp.getProducto());
            detalle.setCantidad(cp.getCantidad());
            detalle.setPrecioHistorico(cp.getPrecioUnidad());
            detalle.setEstado("PENDIENTE");
            pedidoDetalleRepository.save(detalle);
        }

        // Vaciar carrito
        carritoProductoRepository.deleteAll(productosCarrito);

        return convertirADTO(pedido);
    }

    // Cambiar estado del pedido (solo ADMIN)
    public PedidoDTO cambiarEstado(Integer idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));
        pedido.setEstadoPedido(nuevoEstado);
        return convertirADTO(pedidoRepository.save(pedido));
    }

    // Convertir Entity a DTO
    private PedidoDTO convertirADTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setTotal(pedido.getTotal());
        dto.setEstadoPedido(pedido.getEstadoPedido());
        dto.setMetodoPago(pedido.getMetodoPago());

        List<PedidoDetalle> detalles = pedidoDetalleRepository
                .findByPedido_IdPedido(pedido.getIdPedido());

        dto.setDetalles(detalles.stream()
                .map(this::convertirDetalleADTO)
                .collect(Collectors.toList()));

        return dto;
    }

    // Convertir PedidoDetalle Entity a DTO
    private PedidoDetalleDTO convertirDetalleADTO(PedidoDetalle detalle) {
        PedidoDetalleDTO dto = new PedidoDetalleDTO();
        dto.setIdPedidoDetalle(detalle.getIdPedidoDetalle());
        dto.setIdProducto(detalle.getProducto().getIdProducto());
        dto.setNombreProducto(detalle.getProducto().getNombreProducto());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioHistorico(detalle.getPrecioHistorico());
        dto.setEstado(detalle.getEstado());
        return dto;
    }

}
