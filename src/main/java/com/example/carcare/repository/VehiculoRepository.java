package com.example.carcare.repository;

import com.example.carcare.entity.Vehiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends CrudRepository<Vehiculo, Long> {

    List<Vehiculo> findByUsuarioIdUsuario(Long idUsuario);

    @Query(value = "SELECT * FROM vehiculo WHERE id_usuario = :idUsuario", nativeQuery = true)
    List<Vehiculo> findVehiculosByUsuarioId(@Param("idUsuario") Long idUsuario);

    boolean existsByMatriculaAndUsuarioIdUsuario(String matricula, Long idUsuario);

    boolean existsByMatriculaAndUsuarioIdUsuarioAndIdVehiculoNot(String matricula, Long idUsuario, Long idVehiculo);
}