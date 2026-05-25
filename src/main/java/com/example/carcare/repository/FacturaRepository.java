package com.example.carcare.repository;

import com.example.carcare.entity.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends CrudRepository<Factura, Long> {

    /**
     * Busca la factura asociada a un mantenimiento específico
     * @param idMantenimiento ID del mantenimiento
     * @return Optional con la factura si existe
     */
    Optional<Factura> findByMantenimientoIdMantenimiento(Long idMantenimiento);
}