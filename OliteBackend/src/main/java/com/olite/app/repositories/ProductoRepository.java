package com.olite.app.repositories;

import com.olite.app.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre); // Buscador de productos por nombre
}
