package com.example.carcare.repository;

import com.example.carcare.entity.Repostaje;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepostajeRepository extends CrudRepository<Repostaje, Long> {

    List<Repostaje> findByVehiculoIdVehiculoOrderByFechaDesc(Long idVehiculo);

    List<Repostaje> findByVehiculoIdVehiculoAndFecha(Long idVehiculo, LocalDate fecha);

    @Query(value = "SELECT * FROM repostaje WHERE id_vehiculo = :idVehiculo ORDER BY fecha DESC", nativeQuery = true)
    List<Repostaje> findRepostajesByVehiculoId(@Param("idVehiculo") Long idVehiculo);

    @Query(value = "SELECT v.marca, v.modelo, AVG((r.litros * 100) / (r.kilometraje - r_prev.kilometraje)) AS consumo_medio " +
           "FROM repostaje r " +
           "JOIN vehiculo v ON r.id_vehiculo = v.id_vehiculo " +
           "JOIN repostaje r_prev ON r_prev.id_repostaje = (SELECT id_repostaje FROM repostaje WHERE id_vehiculo = v.id_vehiculo AND fecha < r.fecha ORDER BY fecha DESC LIMIT 1) " +
           "WHERE r.kilometraje > r_prev.kilometraje " +
           "GROUP BY v.marca, v.modelo", nativeQuery = true)
    List<Object[]> findConsumoMedioGlobal();

    @Query(value = "SELECT v.marca, v.modelo, v.matricula, SUM(r.litros * r.precio_litro) AS gasto_total " +
           "FROM repostaje r JOIN vehiculo v ON r.id_vehiculo = v.id_vehiculo " +
           "GROUP BY v.id_vehiculo ORDER BY gasto_total DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findVehiculosMayorGasto();
}