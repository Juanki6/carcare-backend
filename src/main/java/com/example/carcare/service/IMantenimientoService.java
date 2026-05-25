package com.example.carcare.service;

import com.example.carcare.dto.MantenimientoDto;
import com.example.carcare.entity.Mantenimiento;

import java.util.List;

public interface IMantenimientoService {

    List<MantenimientoDto> findAll();

    MantenimientoDto findById(Long id);

    List<MantenimientoDto> findByVehiculoId(Long idVehiculo);

    MantenimientoDto save(MantenimientoDto mantenimientoDto);

    MantenimientoDto update(Long id, MantenimientoDto mantenimientoDto);

    void deleteById(Long id);

    Mantenimiento saveEntity(Mantenimiento mantenimiento);
}