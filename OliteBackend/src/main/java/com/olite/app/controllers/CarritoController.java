package com.olite.app.controllers;

import com.olite.app.dto.CarritoDTO;
import com.olite.app.services.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // GET /api/carrito/{idUsuario}
    @GetMapping("/{idUsuario}")
    public ResponseEntity<CarritoDTO> obtenerCarrito(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(idUsuario));
    }

    // POST /api/carrito/{idUsuario}/productos/{idProducto}?cantidad=1
    @PostMapping("/{idUsuario}/productos/{idProducto}")
    public ResponseEntity<CarritoDTO> añadirProducto(@PathVariable Integer idUsuario,
                                                     @PathVariable Integer idProducto,
                                                     @RequestParam(defaultValue = "1") Integer cantidad) {
        return ResponseEntity.ok(carritoService.añadirProducto(idUsuario, idProducto, cantidad));
    }

    // DELETE /api/carrito/{idUsuario}/productos/{idProducto}
    @DeleteMapping("/{idUsuario}/productos/{idProducto}")
    public ResponseEntity<CarritoDTO> eliminarProducto(@PathVariable Integer idUsuario,
                                                       @PathVariable Integer idProducto) {
        return ResponseEntity.ok(carritoService.eliminarProducto(idUsuario, idProducto));
    }

    // DELETE /api/carrito/{idUsuario}/vaciar
    @DeleteMapping("/{idUsuario}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Integer idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.noContent().build();
    }
}