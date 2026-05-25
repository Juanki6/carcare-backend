package com.example.carcare.service;

import com.example.carcare.dto.MantenimientoDto;
import com.example.carcare.entity.Mantenimiento;
import com.example.carcare.entity.Taller;
import com.example.carcare.entity.TipoMantenimiento;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.MantenimientoMapper;
import com.example.carcare.repository.MantenimientoRepository;
import com.example.carcare.repository.TallerRepository;
import com.example.carcare.repository.TipoMantenimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MantenimientoService implements IMantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final MantenimientoMapper mantenimientoMapper;
    private final TipoMantenimientoRepository tipoMantenimientoRepository;
    private final TallerRepository tallerRepository;

    @Override
    public List<MantenimientoDto> findAll() {
        try {
            List<Mantenimiento> mantenimientos = (List<Mantenimiento>) mantenimientoRepository.findAll();
            return mantenimientoMapper.toDTOList(mantenimientos);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todos los mantenimientos", ex);
        }
    }

    @Override
    public MantenimientoDto findById(Long id) {
        try {
            Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Mantenimiento.class.getSimpleName(), id));
            return mantenimientoMapper.toDTO(mantenimiento);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar el mantenimiento con id: " + id, ex);
        }
    }

    @Override
    public List<MantenimientoDto> findByVehiculoId(Long idVehiculo) {
        try {
            List<Mantenimiento> mantenimientos = mantenimientoRepository.findByVehiculoIdVehiculo(idVehiculo);
            return mantenimientoMapper.toDTOList(mantenimientos);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar mantenimientos del vehículo con id: " + idVehiculo, ex);
        }
    }

    @Override
    public MantenimientoDto save(MantenimientoDto mantenimientoDto) {
        try {
            Mantenimiento mantenimiento = mantenimientoMapper.toEntity(mantenimientoDto);
            Mantenimiento saved = mantenimientoRepository.save(mantenimiento);
            return mantenimientoMapper.toDTO(saved);
        } catch (Exception ex) {
            throw new CreateEntityException(Mantenimiento.class.getSimpleName(), mantenimientoDto, ex);
        }
    }

    // Sobrecarga para guardar la entidad directamente desde el controlador (sin pasar por DTO)
    public Mantenimiento save(Mantenimiento mantenimiento) {
        try {
            return mantenimientoRepository.save(mantenimiento);
        } catch (Exception ex) {
            throw new CreateEntityException(Mantenimiento.class.getSimpleName(), mantenimiento, ex);
        }
    }

    // Actualización parcial: solo se sobreescriben los campos del DTO que no sean null,
    // evitando que el cliente tenga que enviar todos los campos para modificar uno solo.
    @Override
    public MantenimientoDto update(Long id, MantenimientoDto mantenimientoDto) {
        try {
            if (!mantenimientoRepository.existsById(id)) {
                throw new NotFoundException(Mantenimiento.class.getSimpleName(), id);
            }

            Mantenimiento mantenimientoExistente = mantenimientoRepository.findById(id).get();

            if (mantenimientoDto.getTipoEnum() != null) {
                mantenimientoExistente.setTipoEnum(Mantenimiento.TipoMantenimientoEnum.valueOf(mantenimientoDto.getTipoEnum()));
            }
            if (mantenimientoDto.getFecha() != null) {
                mantenimientoExistente.setFecha(mantenimientoDto.getFecha());
            }
            if (mantenimientoDto.getKilometraje() != null) {
                mantenimientoExistente.setKilometraje(mantenimientoDto.getKilometraje());
            }
            if (mantenimientoDto.getNotas() != null) {
                mantenimientoExistente.setNotas(mantenimientoDto.getNotas());
            }
            if (mantenimientoDto.getIdTipo() != null) {
                TipoMantenimiento tipo = tipoMantenimientoRepository.findById(mantenimientoDto.getIdTipo())
                        .orElseThrow(() -> new NotFoundException("Tipo de mantenimiento no encontrado con id: " + mantenimientoDto.getIdTipo()));
                mantenimientoExistente.setTipo(tipo);
            }
            if (mantenimientoDto.getIdTaller() != null) {
                Taller taller = tallerRepository.findById(mantenimientoDto.getIdTaller())
                        .orElseThrow(() -> new NotFoundException("Taller no encontrado con id: " + mantenimientoDto.getIdTaller()));
                mantenimientoExistente.setTaller(taller);
            }

            Mantenimiento updated = mantenimientoRepository.save(mantenimientoExistente);
            return mantenimientoMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(Mantenimiento.class.getSimpleName(), mantenimientoDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!mantenimientoRepository.existsById(id)) {
                throw new NotFoundException(Mantenimiento.class.getSimpleName(), id);
            }
            mantenimientoRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(Mantenimiento.class.getSimpleName(), id, ex);
        }
    }

    // Versión definida en la interfaz; equivalente a save(Mantenimiento) pero accesible vía IMantenimientoService
    public Mantenimiento saveEntity(Mantenimiento mantenimiento) {
        try {
            return mantenimientoRepository.save(mantenimiento);
        } catch (Exception ex) {
            throw new CreateEntityException(Mantenimiento.class.getSimpleName(), mantenimiento, ex);
        }
    }
}