package com.olite.app.controllers;

import com.olite.app.dto.ProductoDTO;
import com.olite.app.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // GET /api/productos
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // GET /api/productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // POST /api/productos
    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.crearProducto(dto));
    }

    // PUT /api/productos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Integer id,
                                                          @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
    }

    // DELETE /api/productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}