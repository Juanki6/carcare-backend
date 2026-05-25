package com.example.carcare.service;

import com.example.carcare.dto.UsuarioDto;
import com.example.carcare.entity.Usuario;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.UsuarioMapper;
import com.example.carcare.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDto> findAll() {
        try {
            List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
            return usuarioMapper.toDTOList(usuarios);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todos los usuarios", ex);
        }
    }

    @Override
    public UsuarioDto findById(Long id) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Usuario.class.getSimpleName(), id));
            return usuarioMapper.toDTO(usuario);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar el usuario con id: " + id, ex);
        }
    }

    @Override
    public Optional<UsuarioDto> findByEmail(String email) {
        try {
            return usuarioRepository.findByEmail(email).map(usuarioMapper::toDTO);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar el usuario con email: " + email, ex);
        }
    }

    @Override
    public UsuarioDto save(UsuarioDto usuarioDto) {
        if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
            throw new CreateEntityException("El email ya está registrado");
        }
        try {
            Usuario usuario = usuarioMapper.toEntity(usuarioDto);
            Usuario saved = usuarioRepository.save(usuario);
            return usuarioMapper.toDTO(saved);
        } catch (Exception ex) {
            throw new CreateEntityException(Usuario.class.getSimpleName(), usuarioDto, ex);
        }
    }

    @Override
    public UsuarioDto registro(UsuarioDto usuarioDto) {
        if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
            throw new CreateEntityException("El email ya está registrado");
        }
        try {
            Usuario usuario = usuarioMapper.toEntity(usuarioDto);
            usuario.setPasswordHash(passwordEncoder.encode(usuarioDto.getPasswordHash()));
            usuario.setRol("ROLE_USER");
            Usuario saved = usuarioRepository.save(usuario);
            return usuarioMapper.toDTO(saved);
        } catch (Exception ex) {
            throw new CreateEntityException(Usuario.class.getSimpleName(), usuarioDto, ex);
        }
    }

    @Override
    public UsuarioDto update(Long id, UsuarioDto usuarioDto) {
        try {
            if (!usuarioRepository.existsById(id)) {
                throw new NotFoundException(Usuario.class.getSimpleName(), id);
            }
            Usuario usuario = usuarioMapper.toEntity(usuarioDto);
            usuario.setIdUsuario(id);
            // Si se envía contraseña nueva, hashearla
            if (usuarioDto.getPasswordHash() != null && !usuarioDto.getPasswordHash().isBlank()) {
                usuario.setPasswordHash(passwordEncoder.encode(usuarioDto.getPasswordHash()));
            } else {
                // Mantener la contraseña existente en BD
                usuarioRepository.findById(id).ifPresent(existing ->
                        usuario.setPasswordHash(existing.getPasswordHash()));
            }
            Usuario updated = usuarioRepository.save(usuario);
            return usuarioMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(Usuario.class.getSimpleName(), usuarioDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                throw new NotFoundException(Usuario.class.getSimpleName(), id);
            }
            usuarioRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(Usuario.class.getSimpleName(), id, ex);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return usuarioRepository.existsByEmail(email);
        } catch (Exception ex) {
            throw new ErrorException("Error al comprobar si existe el email: " + email, ex);
        }
    }
}
