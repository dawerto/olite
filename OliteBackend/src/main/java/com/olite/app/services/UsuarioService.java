package com.olite.app.services;

import com.olite.app.dto.UsuarioDTO;
import com.olite.app.entities.Usuario;
import com.olite.app.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios (solo ADMIN)
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener usuario por ID
    public UsuarioDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return convertirADTO(usuario);
    }

    // Actualizar usuario
    public UsuarioDTO actualizarUsuario(Integer id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEmail(dto.getEmail());
        return convertirADTO(usuarioRepository.save(usuario));
    }

    // Eliminar usuario (solo ADMIN)
    public void eliminarUsuario(Integer id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuarioRepository.deleteById(id);
    }

    // Convertir Entity a DTO
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellidos(usuario.getApellidos());
        dto.setDireccion(usuario.getDireccion());
        dto.setTelefono(usuario.getTelefono());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        return dto;
    }

}
