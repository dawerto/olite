package com.olite.app.repositories;

import com.olite.app.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Optional<Carrito> findByUsuario_IdUsuario(Integer idUsuario); // Encontrar carrito del usuario por Id

}
