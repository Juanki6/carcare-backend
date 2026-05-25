package com.example.carcare.repository;

import com.example.carcare.entity.Mantenimiento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MantenimientoRepository extends CrudRepository<Mantenimiento, Long> {

    List<Mantenimiento> findByVehiculoIdVehiculo(Long idVehiculo);

    Optional<Mantenimiento> findTopByVehiculoIdVehiculoAndTipoEnumOrderByFechaDesc(
            Long idVehiculo, Mantenimiento.TipoMantenimientoEnum tipoEnum);

    @Query("SELECT m FROM Mantenimiento m LEFT JOIN FETCH m.factura WHERE m.vehiculo.idVehiculo = :idVehiculo")
    List<Mantenimiento> findByVehiculoIdVehiculoWithFactura(@Param("idVehiculo") Long idVehiculo);

    @Query(value = "SELECT * FROM mantenimiento WHERE id_vehiculo = :idVehiculo ORDER BY fecha DESC", nativeQuery = true)
    List<Mantenimiento> findMantenimientosByVehiculoId(@Param("idVehiculo") Long idVehiculo);
}