package com.olite.app;

import com.olite.app.dto.ProductoDTO;
import com.olite.app.entities.Producto;
import com.olite.app.repositories.ProductoRepository;
import com.olite.app.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductoService.
 * Se utiliza Mockito para simular el repositorio
 * y evitar dependencia con la base de datos.
 */
@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    /**
     * Verifica que obtenerTodos devuelve
     * correctamente la lista de productos.
     */
    @Test
    public void testObtenerTodos() {
        Producto p1 = new Producto();
        p1.setIdProducto(1);
        p1.setNombreProducto("Jabón natural");
        p1.setPrecio(new BigDecimal("5.99"));
        p1.setStock(50);

        Producto p2 = new Producto();
        p2.setIdProducto(2);
        p2.setNombreProducto("Crema hidratante");
        p2.setPrecio(new BigDecimal("8.50"));
        p2.setStock(30);

        when(productoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProductoDTO> resultado = productoService.obtenerTodos();

        assertEquals(2, resultado.size());
        assertEquals("Jabón natural", resultado.get(0).getNombreProducto());
        assertEquals("Crema hidratante", resultado.get(1).getNombreProducto());
    }

    /**
     * Verifica que obtenerPorId devuelve correctamente
     * un ProductoDTO cuando el producto existe en la BD.
     */
    @Test
    public void testObtenerPorId() {
        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombreProducto("Jabón natural");
        producto.setPrecio(new BigDecimal("5.99"));
        producto.setStock(50);

        when(productoRepository.findById(1))
                .thenReturn(Optional.of(producto));

        ProductoDTO resultado = productoService.obtenerPorId(1);

        assertEquals("Jabón natural", resultado.getNombreProducto());
        assertEquals(new BigDecimal("5.99"), resultado.getPrecio());
    }

    /**
     * Verifica que obtenerPorId lanza una excepción
     * cuando el producto no existe en la BD.
     */
    @Test
    public void testObtenerPorIdNoExiste() {
        when(productoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productoService.obtenerPorId(99);
        });
    }

    /**
     * Verifica que crearProducto guarda correctamente
     * un nuevo producto en la BD.
     */
    @Test
    public void testCrearProducto() {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombreProducto("Jabón natural");
        dto.setPrecio(new BigDecimal("5.99"));
        dto.setStock(50);
        dto.setDescripcion("Jabón artesanal");
        dto.setImagen("jabon.jpg");

        Producto productoGuardado = new Producto();
        productoGuardado.setIdProducto(1);
        productoGuardado.setNombreProducto("Jabón natural");
        productoGuardado.setPrecio(new BigDecimal("5.99"));
        productoGuardado.setStock(50);
        productoGuardado.setDescripcion("Jabón artesanal");
        productoGuardado.setImagen("jabon.jpg");

        when(productoRepository.save(any(Producto.class)))
                .thenReturn(productoGuardado);

        ProductoDTO resultado = productoService.crearProducto(dto);

        assertEquals("Jabón natural", resultado.getNombreProducto());
        assertEquals(new BigDecimal("5.99"), resultado.getPrecio());
        assertEquals(1, resultado.getIdProducto());
    }

    /**
     * Verifica que eliminarProducto elimina correctamente
     * un producto existente en la BD.
     */
    @Test
    public void testEliminarProducto() {
        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombreProducto("Jabón natural");
        producto.setPrecio(new BigDecimal("5.99"));
        producto.setStock(50);

        when(productoRepository.findById(1))
                .thenReturn(Optional.of(producto));

        assertDoesNotThrow(() -> productoService.eliminarProducto(1));
        verify(productoRepository, times(1)).deleteById(1);
    }

    /**
     * Verifica que eliminarProducto lanza una excepción
     * cuando el producto no existe en la BD.
     */
    @Test
    public void testEliminarProductoNoExiste() {
        when(productoRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            productoService.eliminarProducto(99);
        });
    }
}