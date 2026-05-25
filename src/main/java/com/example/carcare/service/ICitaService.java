package com.example.carcare.service;

import com.example.carcare.dto.CitaDto;
import com.example.carcare.entity.Cita;

import java.util.List;

public interface ICitaService {

    List<CitaDto> findAll();

    CitaDto findById(Long id);

    List<CitaDto> findByVehiculoId(Long idVehiculo);

    List<CitaDto> findCitasPendientes();

    List<CitaDto> findCitasAtrasadas();

    CitaDto save(CitaDto citaDto);

    CitaDto update(Long id, CitaDto citaDto);

    void deleteById(Long id);

    Cita saveEntity(Cita cita);

    CitaDto marcarComoCompletada(Long id);

    // NUEVO MÉTODO
    CitaDto marcarRecordatorioEnviado(Long id);
}