package com.olite.app.repositories;

import com.olite.app.entities.CarritoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Integer> {
    List<CarritoProducto> findByCarrito_IdCarrito(Integer idCarrito); // Obtener productos del carrito del usuario por su Id
    Optional<CarritoProducto> findByCarrito_IdCarritoAndProducto_IdProducto(Integer idCarrito, Integer idProducto);
}
