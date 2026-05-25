package com.example.carcare.service;

import com.example.carcare.dto.CitaDto;
import com.example.carcare.entity.Cita;
import com.example.carcare.entity.TipoMantenimiento;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.CitaMapper;
import com.example.carcare.repository.CitaRepository;
import com.example.carcare.repository.TipoMantenimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CitaService implements ICitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final TipoMantenimientoRepository tipoMantenimientoRepository;

    @Override
    public List<CitaDto> findAll() {
        try {
            List<Cita> citas = (List<Cita>) citaRepository.findAll();
            return citaMapper.toDTOList(citas);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todas las citas", ex);
        }
    }

    @Override
    public CitaDto findById(Long id) {
        try {
            Cita cita = citaRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Cita.class.getSimpleName(), id));
            return citaMapper.toDTO(cita);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar la cita con id: " + id, ex);
        }
    }

    @Override
    public List<CitaDto> findByVehiculoId(Long idVehiculo) {
        try {
            List<Cita> citas = citaRepository.findByVehiculoIdVehiculo(idVehiculo);
            return citaMapper.toDTOList(citas);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar citas del vehículo con id: " + idVehiculo, ex);
        }
    }

    @Override
    public List<CitaDto> findCitasPendientes() {
        try {
            List<Cita> citas = citaRepository.findByCompletadaFalse();
            return citaMapper.toDTOList(citas);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar citas pendientes", ex);
        }
    }

    @Override
    public List<CitaDto> findCitasAtrasadas() {
        try {
            List<Cita> citas = citaRepository.findByCompletadaFalseAndFechaProgramadaBefore(LocalDate.now());
            return citaMapper.toDTOList(citas);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar citas atrasadas", ex);
        }
    }

    @Override
    public CitaDto save(CitaDto citaDto) {
        try {
            Cita cita = citaMapper.toEntity(citaDto);
            Cita saved = citaRepository.save(cita);
            return citaMapper.toDTO(saved);
        } catch (Exception ex) {
            throw new CreateEntityException(Cita.class.getSimpleName(), citaDto, ex);
        }
    }

    @Override
    public CitaDto update(Long id, CitaDto citaDto) {
        try {
            if (!citaRepository.existsById(id)) {
                throw new NotFoundException(Cita.class.getSimpleName(), id);
            }

            // Obtener la cita existente
            Cita citaExistente = citaRepository.findById(id).get();

            // Actualizar solo los campos que vienen en el DTO
            if (citaDto.getTitulo() != null) {
                citaExistente.setTitulo(citaDto.getTitulo());
            }
            if (citaDto.getFechaProgramada() != null) {
                citaExistente.setFechaProgramada(citaDto.getFechaProgramada());
            }
            if (citaDto.getKilometrajeProgramado() != null) {
                citaExistente.setKilometrajeProgramado(citaDto.getKilometrajeProgramado());
            }
            if (citaDto.getNotas() != null) {
                citaExistente.setNotas(citaDto.getNotas());
            }
            if (citaDto.getCompletada() != null) {
                citaExistente.setCompletada(citaDto.getCompletada());
            }

            // Manejar el tipo de mantenimiento
            if (citaDto.getIdTipoMantenimiento() != null) {
                TipoMantenimiento tipo = tipoMantenimientoRepository.findById(citaDto.getIdTipoMantenimiento())
                        .orElseThrow(() -> new NotFoundException("Tipo de mantenimiento no encontrado"));
                citaExistente.setTipoMantenimiento(tipo);
            } else {
                citaExistente.setTipoMantenimiento(null);
            }

            Cita updated = citaRepository.save(citaExistente);
            return citaMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(Cita.class.getSimpleName(), citaDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!citaRepository.existsById(id)) {
                throw new NotFoundException(Cita.class.getSimpleName(), id);
            }
            citaRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(Cita.class.getSimpleName(), id, ex);
        }
    }

    @Override
    public Cita saveEntity(Cita cita) {
        try {
            return citaRepository.save(cita);
        } catch (Exception ex) {
            throw new CreateEntityException(Cita.class.getSimpleName(), cita, ex);
        }
    }

    @Override
    public CitaDto marcarComoCompletada(Long id) {
        try {
            Cita cita = citaRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Cita.class.getSimpleName(), id));
            cita.setCompletada(true);
            Cita updated = citaRepository.save(cita);
            return citaMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al marcar como completada la cita con id: " + id, ex);
        }
    }

    @Override
    @Transactional
    public CitaDto marcarRecordatorioEnviado(Long id) {
        try {
            Cita cita = citaRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Cita.class.getSimpleName(), id));
            cita.setRecordatorioEnviado(true);
            Cita updated = citaRepository.save(cita);
            return citaMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al marcar recordatorio como enviado en cita con id: " + id, ex);
        }
    }
}
