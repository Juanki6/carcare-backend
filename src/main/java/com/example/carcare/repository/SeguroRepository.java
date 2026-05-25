package com.example.carcare.repository;

import com.example.carcare.entity.Seguro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeguroRepository extends CrudRepository<Seguro, Long> {

    Optional<Seguro> findByVehiculoIdVehiculo(Long idVehiculo);

    List<Seguro> findByVehiculoIdVehiculoIn(List<Long> ids);
}