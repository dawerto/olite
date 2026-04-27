package com.olite.app.controllers;

import com.olite.app.dto.PedidoDTO;
import com.olite.app.services.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // GET /api/pedidos/todos (solo ADMIN)
    @GetMapping("/todos")
    public ResponseEntity<List<PedidoDTO>> obtenerTodos() {
        return ResponseEntity.ok(pedidoService.obtenerTodos());
    }

    // GET /api/pedidos/usuario/{idUsuario}
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosUsuario(idUsuario));
    }

    // GET /api/pedidos/{idPedido}
    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoDTO> obtenerPorId(@PathVariable Integer idPedido) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(idPedido));
    }

    // POST /api/pedidos/{idUsuario}?metodoPago=Tarjeta
    @PostMapping("/{idUsuario}")
    public ResponseEntity<PedidoDTO> realizarPedido(@PathVariable Integer idUsuario,
                                                    @RequestParam(defaultValue = "Tarjeta") String metodoPago) {
        return ResponseEntity.ok(pedidoService.realizarPedido(idUsuario, metodoPago));
    }

    // PUT /api/pedidos/estado/{idPedido}?nuevoEstado=ENVIADO (solo ADMIN)
    @PutMapping("/estado/{idPedido}")
    public ResponseEntity<PedidoDTO> cambiarEstado(@PathVariable Integer idPedido,
                                                   @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(idPedido, nuevoEstado));
    }
}