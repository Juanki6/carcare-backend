package com.example.carcare.repository;

import com.example.carcare.entity.TipoMantenimiento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoMantenimientoRepository extends CrudRepository<TipoMantenimiento, Long> {

    /**
     * Busca un tipo de mantenimiento por su nombre
     * @param nombreTipo Nombre del tipo (ej: "Cambio de aceite", "ITV")
     * @return Optional con el tipo si existe
     */
    Optional<TipoMantenimiento> findByNombreTipo(String nombreTipo);
}