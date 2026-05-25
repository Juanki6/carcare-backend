package com.example.carcare.service;

import com.example.carcare.dto.SeguroDto;
import com.example.carcare.entity.Seguro;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.SeguroMapper;
import com.example.carcare.repository.SeguroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SeguroService implements ISeguroService {

    private final SeguroRepository seguroRepository;
    private final SeguroMapper seguroMapper;

    @Override
    public List<SeguroDto> findAll() {
        try {
            List<Seguro> seguros = (List<Seguro>) seguroRepository.findAll();
            return seguroMapper.toDTOList(seguros);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todos los seguros", ex);
        }
    }

    @Override
    public SeguroDto findById(Long id) {
        try {
            Seguro seguro = seguroRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Seguro.class.getSimpleName(), id));
            return seguroMapper.toDTO(seguro);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar el seguro con id: " + id, ex);
        }
    }

    @Override
    public Optional<SeguroDto> findByVehiculoId(Long idVehiculo) {
        try {
            return seguroRepository.findByVehiculoIdVehiculo(idVehiculo).map(seguroMapper::toDTO);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar seguro del vehículo con id: " + idVehiculo, ex);
        }
    }

    @Override
    public SeguroDto save(SeguroDto seguroDto) {
        try {
            Seguro seguro = seguroMapper.toEntity(seguroDto);
            Seguro saved = seguroRepository.save(seguro);
            return seguroMapper.toDTO(saved);
        } catch (Exception ex) {
            throw new CreateEntityException(Seguro.class.getSimpleName(), seguroDto, ex);
        }
    }

    @Override
    public SeguroDto update(Long id, SeguroDto seguroDto) {
        try {
            if (!seguroRepository.existsById(id)) {
                throw new NotFoundException(Seguro.class.getSimpleName(), id);
            }
            Seguro seguro = seguroMapper.toEntity(seguroDto);
            seguro.setIdSeguro(id);
            Seguro updated = seguroRepository.save(seguro);
            return seguroMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(Seguro.class.getSimpleName(), seguroDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!seguroRepository.existsById(id)) {
                throw new NotFoundException(Seguro.class.getSimpleName(), id);
            }
            seguroRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(Seguro.class.getSimpleName(), id, ex);
        }
    }

    @Override
    public Seguro saveEntity(Seguro seguro) {
        try {
            return seguroRepository.save(seguro);
        } catch (Exception ex) {
            throw new CreateEntityException(Seguro.class.getSimpleName(), seguro, ex);
        }
    }
}
