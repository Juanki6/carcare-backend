package com.example.carcare.service;

import com.example.carcare.dto.VehiculoDto;
import com.example.carcare.entity.Vehiculo;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.VehiculoMapper;
import com.example.carcare.repository.VehiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VehiculoService implements IVehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final VehiculoMapper vehiculoMapper;

    @Override
    public List<VehiculoDto> findAll() {
        try {
            List<Vehiculo> vehiculos = (List<Vehiculo>) vehiculoRepository.findAll();
            return vehiculoMapper.toDTOList(vehiculos);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todos los vehículos", ex);
        }
    }

    @Override
    public VehiculoDto findById(Long id) {
        try {
            Vehiculo vehiculo = vehiculoRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Vehiculo.class.getSimpleName(), id));
            return vehiculoMapper.toDTO(vehiculo);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar el vehículo con id: " + id, ex);
        }
    }

    @Override
    public List<VehiculoDto> findByUsuarioId(Long idUsuario) {
        try {
            List<Vehiculo> vehiculos = vehiculoRepository.findByUsuarioIdUsuario(idUsuario);
            return vehiculoMapper.toDTOList(vehiculos);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar vehículos del usuario con id: " + idUsuario, ex);
        }
    }

    @Override
    public VehiculoDto save(VehiculoDto vehiculoDto) {
        if (vehiculoRepository.existsByMatriculaAndUsuarioIdUsuario(vehiculoDto.getMatricula(), vehiculoDto.getIdUsuario())) {
            throw new CreateEntityException("Ya tienes un vehículo con esta matrícula");
        }
        try {
            Vehiculo vehiculo = vehiculoMapper.toEntity(vehiculoDto);
            Vehiculo saved = vehiculoRepository.save(vehiculo);
            return vehiculoMapper.toDTO(saved);
        } catch (Exception ex) {
            throw new CreateEntityException(Vehiculo.class.getSimpleName(), vehiculoDto, ex);
        }
    }

    @Override
    public VehiculoDto update(Long id, VehiculoDto vehiculoDto) {
        if (!vehiculoRepository.existsById(id)) {
            throw new NotFoundException(Vehiculo.class.getSimpleName(), id);
        }
        if (vehiculoRepository.existsByMatriculaAndUsuarioIdUsuarioAndIdVehiculoNot(vehiculoDto.getMatricula(), vehiculoDto.getIdUsuario(), id)) {
            throw new CreateEntityException("Ya tienes un vehículo con esta matrícula");
        }
        try {
            Vehiculo vehiculo = vehiculoMapper.toEntity(vehiculoDto);
            vehiculo.setIdVehiculo(id);
            Vehiculo updated = vehiculoRepository.save(vehiculo);
            return vehiculoMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(Vehiculo.class.getSimpleName(), vehiculoDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!vehiculoRepository.existsById(id)) {
                throw new NotFoundException(Vehiculo.class.getSimpleName(), id);
            }
            vehiculoRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(Vehiculo.class.getSimpleName(), id, ex);
        }
    }
}