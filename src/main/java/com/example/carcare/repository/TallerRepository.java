package com.example.carcare.repository;

import com.example.carcare.entity.Taller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TallerRepository extends CrudRepository<Taller, Long> {

    /**
     * Busca talleres cuyo nombre contenga el texto (ignorando mayúsculas/minúsculas)
     * @param nombre Texto a buscar en el nombre del taller
     * @return Lista de talleres que coinciden
     */
    List<Taller> findByNombreContainingIgnoreCase(String nombre);

    List<Taller> findByNombreIgnoreCase(String nombre);

    List<Taller> findByLatitudBetweenAndLongitudBetween(
            Double minLat, Double maxLat, Double minLon, Double maxLon);
}