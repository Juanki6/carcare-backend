package com.example.carcare.service;

import com.example.carcare.dto.RepostajeDto;
import com.example.carcare.dto.RepostajeSaveResponse;
import com.example.carcare.entity.Repostaje;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.RepostajeMapper;
import com.example.carcare.repository.RepostajeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RepostajeService implements IRepostajeService {

    private final RepostajeRepository repostajeRepository;
    private final RepostajeMapper repostajeMapper;

    @Override
    public List<RepostajeDto> findAll() {
        try {
            List<Repostaje> repostajes = (List<Repostaje>) repostajeRepository.findAll();
            return repostajeMapper.toDTOList(repostajes);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todos los repostajes", ex);
        }
    }

    @Override
    public RepostajeDto findById(Long id) {
        try {
            Repostaje repostaje = repostajeRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Repostaje.class.getSimpleName(), id));
            return repostajeMapper.toDTO(repostaje);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar el repostaje con id: " + id, ex);
        }
    }

    @Override
    public List<RepostajeDto> findByVehiculoId(Long idVehiculo) {
        try {
            List<Repostaje> repostajes = repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(idVehiculo);
            return repostajeMapper.toDTOList(repostajes);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar repostajes del vehículo con id: " + idVehiculo, ex);
        }
    }

    // Guarda un repostaje con detección de posibles duplicados.
    // Antes de insertar, busca repostajes del mismo vehículo y fecha con litros similares
    // (diferencia ≤ 0.01 L). Si encuentra uno y forzar=false, devuelve una advertencia
    // en lugar de guardar, para que el usuario confirme.
    @Override
    public RepostajeSaveResponse save(RepostajeDto repostajeDto, boolean forzar) {
        if (!forzar) {
            BigDecimal margen = new BigDecimal("0.01");
            List<Repostaje> candidatos = repostajeRepository
                    .findByVehiculoIdVehiculoAndFecha(repostajeDto.getIdVehiculo(), repostajeDto.getFecha());
            Optional<Repostaje> duplicado = candidatos.stream()
                    .filter(r ->
                            r.getLitros().subtract(repostajeDto.getLitros()).abs().compareTo(margen) <= 0)
                    .findFirst();
            if (duplicado.isPresent()) {
                return new RepostajeSaveResponse(
                        true,
                        "Ya existe un repostaje similar para este vehículo en la misma fecha. ¿Deseas guardarlo igualmente?",
                        repostajeMapper.toDTO(duplicado.get())
                );
            }
        }
        try {
            Repostaje repostaje = repostajeMapper.toEntity(repostajeDto);
            Repostaje saved = repostajeRepository.save(repostaje);
            return new RepostajeSaveResponse(false, null, repostajeMapper.toDTO(saved));
        } catch (Exception ex) {
            throw new CreateEntityException(Repostaje.class.getSimpleName(), repostajeDto, ex);
        }
    }

    @Override
    public RepostajeDto update(Long id, RepostajeDto repostajeDto) {
        try {
            if (!repostajeRepository.existsById(id)) {
                throw new NotFoundException(Repostaje.class.getSimpleName(), id);
            }
            Repostaje repostaje = repostajeMapper.toEntity(repostajeDto);
            repostaje.setIdRepostaje(id);
            Repostaje updated = repostajeRepository.save(repostaje);
            return repostajeMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(Repostaje.class.getSimpleName(), repostajeDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!repostajeRepository.existsById(id)) {
                throw new NotFoundException(Repostaje.class.getSimpleName(), id);
            }
            repostajeRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(Repostaje.class.getSimpleName(), id, ex);
        }
    }
}