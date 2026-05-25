package com.example.carcare.service;

import com.example.carcare.dto.FacturaDto;

import java.util.List;
import java.util.Optional;

public interface IFacturaService {

    /** Obtiene todas las facturas */
    List<FacturaDto> findAll();

    /** Busca una factura por su ID */
    FacturaDto findById(Long id);

    /** Busca la factura asociada a un mantenimiento específico */
    Optional<FacturaDto> findByMantenimientoId(Long idMantenimiento);

    /** Crea una nueva factura */
    FacturaDto save(FacturaDto facturaDto);

    /** Actualiza una factura existente */
    FacturaDto update(Long id, FacturaDto facturaDto);

    /** Elimina una factura por su ID */
    void deleteById(Long id);
}