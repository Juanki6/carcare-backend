package com.example.carcare.service;

import com.example.carcare.dto.TipoMantenimientoDto;
import com.example.carcare.entity.TipoMantenimiento;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.TipoMantenimientoMapper;
import com.example.carcare.repository.TipoMantenimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TipoMantenimientoService implements ITipoMantenimientoService {

    private final TipoMantenimientoRepository tipoMantenimientoRepository;
    private final TipoMantenimientoMapper tipoMantenimientoMapper;

    @Override
    public List<TipoMantenimientoDto> findAll() {
        try {
            List<TipoMantenimiento> tipos = (List<TipoMantenimiento>) tipoMantenimientoRepository.findAll();
            return tipoMantenimientoMapper.toDTOList(tipos);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todos los tipos de mantenimiento", ex);
        }
    }

    @Override
    public TipoMantenimientoDto findById(Long id) {
        try {
            TipoMantenimiento tipo = tipoMantenimientoRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(TipoMantenimiento.class.getSimpleName(), id));
            return tipoMantenimientoMapper.toDTO(tipo);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar el tipo de mantenimiento con id: " + id, ex);
        }
    }

    @Override
    public Optional<TipoMantenimientoDto> findByNombreTipo(String nombreTipo) {
        try {
            return tipoMantenimientoRepository.findByNombreTipo(nombreTipo).map(tipoMantenimientoMapper::toDTO);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar tipo de mantenimiento por nombre: " + nombreTipo, ex);
        }
    }

    @Override
    public TipoMantenimientoDto save(TipoMantenimientoDto tipoMantenimientoDto) {
        try {
            TipoMantenimiento tipo = tipoMantenimientoMapper.toEntity(tipoMantenimientoDto);
            TipoMantenimiento saved = tipoMantenimientoRepository.save(tipo);
            return tipoMantenimientoMapper.toDTO(saved);
        } catch (Exception ex) {
            throw new CreateEntityException(TipoMantenimiento.class.getSimpleName(), tipoMantenimientoDto, ex);
        }
    }

    @Override
    public TipoMantenimientoDto update(Long id, TipoMantenimientoDto tipoMantenimientoDto) {
        try {
            if (!tipoMantenimientoRepository.existsById(id)) {
                throw new NotFoundException(TipoMantenimiento.class.getSimpleName(), id);
            }
            TipoMantenimiento tipo = tipoMantenimientoMapper.toEntity(tipoMantenimientoDto);
            tipo.setIdTipo(id);
            TipoMantenimiento updated = tipoMantenimientoRepository.save(tipo);
            return tipoMantenimientoMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(TipoMantenimiento.class.getSimpleName(), tipoMantenimientoDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!tipoMantenimientoRepository.existsById(id)) {
                throw new NotFoundException(TipoMantenimiento.class.getSimpleName(), id);
            }
            tipoMantenimientoRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(TipoMantenimiento.class.getSimpleName(), id, ex);
        }
    }
}