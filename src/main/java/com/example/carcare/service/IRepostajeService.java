package com.example.carcare.service;

import com.example.carcare.dto.RepostajeDto;
import com.example.carcare.dto.RepostajeSaveResponse;

import java.util.List;

public interface IRepostajeService {

    /** Obtiene todos los repostajes */
    List<RepostajeDto> findAll();

    /** Busca un repostaje por su ID */
    RepostajeDto findById(Long id);

    /** Busca todos los repostajes de un vehículo específico (ordenados por fecha descendente) */
    List<RepostajeDto> findByVehiculoId(Long idVehiculo);

    /**
     * Crea un nuevo repostaje. Si forzar=false y se detecta un posible duplicado,
     * devuelve la respuesta con duplicado=true sin guardar.
     */
    RepostajeSaveResponse save(RepostajeDto repostajeDto, boolean forzar);

    /** Actualiza un repostaje existente */
    RepostajeDto update(Long id, RepostajeDto repostajeDto);

    /** Elimina un repostaje por su ID */
    void deleteById(Long id);
}