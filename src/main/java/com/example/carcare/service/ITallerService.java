package com.example.carcare.service;

import com.example.carcare.dto.TallerDto;
import com.example.carcare.dto.TallerSaveResponse;

import java.util.List;

public interface ITallerService {

    /** Obtiene todos los talleres */
    List<TallerDto> findAll();

    /** Busca un taller por su ID */
    TallerDto findById(Long id);

    /** Busca talleres cuyo nombre contenga el texto (ignorando mayúsculas/minúsculas) */
    List<TallerDto> findByNombreContaining(String nombre);

    /**
     * Crea un nuevo taller. Si confirmarDuplicado=false y existe un taller con
     * el mismo nombre, devuelve duplicado=true sin guardar.
     */
    TallerSaveResponse save(TallerDto tallerDto, boolean confirmarDuplicado);

    /** Actualiza un taller existente */
    TallerDto update(Long id, TallerDto tallerDto);

    /** Elimina un taller por su ID */
    void deleteById(Long id);
}