package com.olite.app.services;

import com.olite.app.dto.LoginRequestDTO;
import com.olite.app.dto.RegisterRequestDTO;
import com.olite.app.entities.Carrito;
import com.olite.app.entities.Usuario;
import com.olite.app.repositories.CarritoRepository;
import com.olite.app.repositories.UsuarioRepository;
import com.olite.app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Registro de nuevo usuario
    public Map<String, String> register(RegisterRequestDTO dto) {

        // Comprobar si el email ya existe
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol("ROLE_USER");
        usuarioRepository.save(usuario);

        // Crear carrito vacío para el usuario
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carritoRepository.save(carrito);

        // Generar token
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("token", token);
        respuesta.put("rol", usuario.getRol());
        respuesta.put("email", usuario.getEmail());
        return respuesta;
    }

    // Login
    public Map<String, String> login(LoginRequestDTO dto) {

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));

        // Comprobar contraseña
        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }

        // Generar token
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("token", token);
        respuesta.put("rol", usuario.getRol());
        respuesta.put("email", usuario.getEmail());
        respuesta.put("idUsuario", usuario.getIdUsuario().toString());
        return respuesta;
    }
}
