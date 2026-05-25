package com.example.carcare.service;

import com.example.carcare.dto.TipoMantenimientoDto;

import java.util.List;
import java.util.Optional;

public interface ITipoMantenimientoService {

    /** Obtiene todos los tipos de mantenimiento */
    List<TipoMantenimientoDto> findAll();

    /** Busca un tipo de mantenimiento por su ID */
    TipoMantenimientoDto findById(Long id);

    /** Busca un tipo de mantenimiento por su nombre */
    Optional<TipoMantenimientoDto> findByNombreTipo(String nombreTipo);

    /** Crea un nuevo tipo de mantenimiento */
    TipoMantenimientoDto save(TipoMantenimientoDto tipoMantenimientoDto);

    /** Actualiza un tipo de mantenimiento existente */
    TipoMantenimientoDto update(Long id, TipoMantenimientoDto tipoMantenimientoDto);

    /** Elimina un tipo de mantenimiento por su ID */
    void deleteById(Long id);
}