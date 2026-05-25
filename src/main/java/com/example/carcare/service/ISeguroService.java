package com.example.carcare.service;

import com.example.carcare.dto.SeguroDto;
import com.example.carcare.entity.Seguro;

import java.util.List;
import java.util.Optional;

public interface ISeguroService {

    List<SeguroDto> findAll();

    SeguroDto findById(Long id);

    Optional<SeguroDto> findByVehiculoId(Long idVehiculo);

    SeguroDto save(SeguroDto seguroDto);

    SeguroDto update(Long id, SeguroDto seguroDto);

    void deleteById(Long id);

    Seguro saveEntity(Seguro seguro);
}
