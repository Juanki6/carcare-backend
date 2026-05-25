package com.example.carcare.service;

import com.example.carcare.dto.VehiculoDto;

import java.util.List;

public interface IVehiculoService {

    /** Obtiene todos los vehículos */
    List<VehiculoDto> findAll();

    /** Busca un vehículo por su ID */
    VehiculoDto findById(Long id);

    /** Busca todos los vehículos de un usuario específico */
    List<VehiculoDto> findByUsuarioId(Long idUsuario);

    /** Crea un nuevo vehículo */
    VehiculoDto save(VehiculoDto vehiculoDto);

    /** Actualiza un vehículo existente */
    VehiculoDto update(Long id, VehiculoDto vehiculoDto);

    /** Elimina un vehículo por su ID */
    void deleteById(Long id);
}