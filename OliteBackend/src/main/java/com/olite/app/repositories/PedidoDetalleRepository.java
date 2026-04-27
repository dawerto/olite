package com.olite.app.repositories;

import com.olite.app.entities.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Integer> {
    List<PedidoDetalle> findByPedido_IdPedido(Integer idPedido); // Obtener los detalles del pedido
}
