package com.example.carcare.service;

import com.example.carcare.dto.UsuarioDto;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    /** Obtiene todos los usuarios */
    List<UsuarioDto> findAll();

    /** Busca un usuario por su ID */
    UsuarioDto findById(Long id);

    /** Busca un usuario por su email (para login) */
    Optional<UsuarioDto> findByEmail(String email);

    /** Crea un nuevo usuario */
    UsuarioDto save(UsuarioDto usuarioDto);

    /** Actualiza un usuario existente */
    UsuarioDto update(Long id, UsuarioDto usuarioDto);

    /** Elimina un usuario por su ID */
    void deleteById(Long id);

    /** Comprueba si ya existe un usuario con ese email (para registro) */
    boolean existsByEmail(String email);

    /** Registra un nuevo usuario hasheando su contraseña y asignando ROLE_USER */
    UsuarioDto registro(UsuarioDto usuarioDto);
}