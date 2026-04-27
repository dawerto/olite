package com.olite.app;

import com.olite.app.dto.LoginRequestDTO;
import com.olite.app.entities.Usuario;
import com.olite.app.repositories.CarritoRepository;
import com.olite.app.repositories.UsuarioRepository;
import com.olite.app.security.JwtUtil;
import com.olite.app.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthService.
 * Se utiliza Mockito para simular el repositorio,
 * el encoder de contraseñas y el generador de tokens JWT.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    /**
     * Verifica que el login devuelve un token JWT
     * cuando el email y la contraseña son correctos.
     */
    @Test
    public void testLoginCorrecto() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("dani@ejemplo.com");
        dto.setPassword("123456");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setEmail("dani@ejemplo.com");
        usuario.setPassword("hashedPassword");
        usuario.setRol("ROLE_USER");

        when(usuarioRepository.findByEmail("dani@ejemplo.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "hashedPassword"))
                .thenReturn(true);
        when(jwtUtil.generarToken("dani@ejemplo.com", "ROLE_USER"))
                .thenReturn("token123");

        Map<String, String> resultado = authService.login(dto);

        assertEquals("token123", resultado.get("token"));
        assertEquals("ROLE_USER", resultado.get("rol"));
        assertEquals("dani@ejemplo.com", resultado.get("email"));
    }

    /**
     * Verifica que el login lanza una excepción
     * cuando el email no existe en la BD.
     */
    @Test
    public void testLoginEmailIncorrecto() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("noexiste@ejemplo.com");
        dto.setPassword("123456");

        when(usuarioRepository.findByEmail("noexiste@ejemplo.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            authService.login(dto);
        });
    }

    /**
     * Verifica que el login lanza una excepción
     * cuando la contraseña es incorrecta.
     */
    @Test
    public void testLoginPasswordIncorrecto() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("dani@ejemplo.com");
        dto.setPassword("passwordincorrecto");

        Usuario usuario = new Usuario();
        usuario.setEmail("dani@ejemplo.com");
        usuario.setPassword("hashedPassword");
        usuario.setRol("ROLE_USER");

        when(usuarioRepository.findByEmail("dani@ejemplo.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("passwordincorrecto", "hashedPassword"))
                .thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            authService.login(dto);
        });
    }
}
