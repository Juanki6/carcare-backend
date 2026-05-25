package com.example.carcare.repository;

import com.example.carcare.entity.Cita;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends CrudRepository<Cita, Long> {

    List<Cita> findByVehiculoIdVehiculo(Long idVehiculo);

    List<Cita> findByCompletadaFalse();

    List<Cita> findByCompletadaFalseAndFechaProgramadaBefore(LocalDate fecha);

    @Query(value = "SELECT * FROM cita WHERE id_vehiculo = :idVehiculo AND completada = 0 AND fecha_programada >= CURDATE() ORDER BY fecha_programada ASC", nativeQuery = true)
    List<Cita> findCitasPendientesByVehiculoId(@Param("idVehiculo") Long idVehiculo);
}