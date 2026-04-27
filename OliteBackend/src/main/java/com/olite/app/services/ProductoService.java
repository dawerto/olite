package com.olite.app.services;

import com.olite.app.dto.ProductoDTO;
import com.olite.app.entities.Producto;
import com.olite.app.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ProductoService {
    private final ProductoRepository productoRepository;

    // Obtener productos (TODOS)
    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener producto por ID
    public ProductoDTO obtenerPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        return convertirADTO(producto);
    }

    // Crear producto (solo ADMIN)
    public ProductoDTO crearProducto(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombreProducto(dto.getNombreProducto());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagen(dto.getImagen());
        return convertirADTO(productoRepository.save(producto));
    }

    // Actualizar producto (solo ADMIN)
    public ProductoDTO actualizarProducto(Integer id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        producto.setNombreProducto(dto.getNombreProducto());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagen(dto.getImagen());
        return convertirADTO(productoRepository.save(producto));
    }

    // Eliminar producto (solo ADMIN)
    public void eliminarProducto(Integer id) {
        productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        productoRepository.deleteById(id);
    }

    // Convertir Entity a DTO
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagen(producto.getImagen());
        return dto;
    }
}
