package com.example.carcare.service;

import com.example.carcare.dto.UsuarioDto;
import com.example.carcare.entity.Usuario;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.mapper.UsuarioMapper;
import com.example.carcare.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock UsuarioMapper usuarioMapper;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks UsuarioService usuarioService;

    // ─── registro ─────────────────────────────────────────────────────────────

    @Test
    void registro_lanzaExcepcion_siEmailYaExiste() {
        when(usuarioRepository.existsByEmail("test@mail.com")).thenReturn(true);

        UsuarioDto dto = new UsuarioDto();
        dto.setEmail("test@mail.com");
        dto.setPasswordHash("1234");

        assertThatThrownBy(() -> usuarioService.registro(dto))
                .isInstanceOf(CreateEntityException.class)
                .hasMessageContaining("email ya está registrado");
    }

    @Test
    void registro_asignaRolUser_yHasheaContrasena() {
        UsuarioDto dto = new UsuarioDto();
        dto.setEmail("nuevo@mail.com");
        dto.setPasswordHash("planaTexto");
        dto.setNombre("Juan");

        Usuario entidad = new Usuario();
        entidad.setEmail("nuevo@mail.com");
        entidad.setPasswordHash("planaTexto");

        Usuario guardado = new Usuario();
        guardado.setIdUsuario(1L);
        guardado.setEmail("nuevo@mail.com");
        guardado.setPasswordHash("$2a$hash");
        guardado.setRol("ROLE_USER");

        UsuarioDto respuestaDto = new UsuarioDto();
        respuestaDto.setIdUsuario(1L);
        respuestaDto.setEmail("nuevo@mail.com");
        respuestaDto.setRol("ROLE_USER");

        when(usuarioRepository.existsByEmail("nuevo@mail.com")).thenReturn(false);
        when(usuarioMapper.toEntity(dto)).thenReturn(entidad);
        when(passwordEncoder.encode("planaTexto")).thenReturn("$2a$hash");
        when(usuarioRepository.save(any())).thenReturn(guardado);
        when(usuarioMapper.toDTO(guardado)).thenReturn(respuestaDto);

        UsuarioDto resultado = usuarioService.registro(dto);

        assertThat(resultado.getRol()).isEqualTo("ROLE_USER");
        verify(passwordEncoder).encode("planaTexto");
        verify(usuarioRepository).save(argThat(u -> "$2a$hash".equals(u.getPasswordHash())
                && "ROLE_USER".equals(u.getRol())));
    }

    // ─── findById ──────────────────────────────────────────────────────────────

    @Test
    void findById_lanzaNotFoundException_siUsuarioNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.findById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findById_devuelveDto_siUsuarioExiste() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setEmail("a@b.com");

        UsuarioDto dto = new UsuarioDto();
        dto.setIdUsuario(1L);
        dto.setEmail("a@b.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(dto);

        UsuarioDto resultado = usuarioService.findById(1L);

        assertThat(resultado.getEmail()).isEqualTo("a@b.com");
    }

    // ─── existsByEmail ─────────────────────────────────────────────────────────

    @Test
    void existsByEmail_devuelveTrue_siEmailRegistrado() {
        when(usuarioRepository.existsByEmail("existe@mail.com")).thenReturn(true);

        assertThat(usuarioService.existsByEmail("existe@mail.com")).isTrue();
    }
}
