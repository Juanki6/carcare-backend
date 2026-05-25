package com.example.carcare.service;

import com.example.carcare.dto.TallerDto;
import com.example.carcare.dto.TallerSaveResponse;
import com.example.carcare.entity.Taller;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.TallerMapper;
import com.example.carcare.repository.TallerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TallerService implements ITallerService {

    private final TallerRepository tallerRepository;
    private final TallerMapper tallerMapper;

    @Override
    public List<TallerDto> findAll() {
        try {
            List<Taller> talleres = (List<Taller>) tallerRepository.findAll();
            return tallerMapper.toDTOList(talleres);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todos los talleres", ex);
        }
    }

    @Override
    public TallerDto findById(Long id) {
        try {
            Taller taller = tallerRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Taller.class.getSimpleName(), id));
            return tallerMapper.toDTO(taller);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar el taller con id: " + id, ex);
        }
    }

    @Override
    public List<TallerDto> findByNombreContaining(String nombre) {
        try {
            List<Taller> talleres = tallerRepository.findByNombreContainingIgnoreCase(nombre);
            return tallerMapper.toDTOList(talleres);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar talleres por nombre: " + nombre, ex);
        }
    }

    // Guarda un taller con detección de nombre duplicado (case-insensitive).
    // Si ya existe un taller con el mismo nombre y confirmarDuplicado=false,
    // devuelve una advertencia para que el usuario decida si continuar.
    @Override
    public TallerSaveResponse save(TallerDto tallerDto, boolean confirmarDuplicado) {
        if (!confirmarDuplicado) {
            List<Taller> mismoNombre = tallerRepository.findByNombreIgnoreCase(tallerDto.getNombre().trim());
            if (!mismoNombre.isEmpty()) {
                String nombre = mismoNombre.get(0).getNombre();
                return new TallerSaveResponse(
                        true,
                        "Ya existe un taller con el nombre '" + nombre + "'. ¿Deseas crearlo de todas formas?",
                        tallerMapper.toDTO(mismoNombre.get(0))
                );
            }
        }
        try {
            Taller taller = tallerMapper.toEntity(tallerDto);
            Taller saved = tallerRepository.save(taller);
            return new TallerSaveResponse(false, null, tallerMapper.toDTO(saved));
        } catch (Exception ex) {
            throw new CreateEntityException(Taller.class.getSimpleName(), tallerDto, ex);
        }
    }

    @Override
    public TallerDto update(Long id, TallerDto tallerDto) {
        try {
            if (!tallerRepository.existsById(id)) {
                throw new NotFoundException(Taller.class.getSimpleName(), id);
            }
            Taller taller = tallerMapper.toEntity(tallerDto);
            taller.setIdTaller(id);
            Taller updated = tallerRepository.save(taller);
            return tallerMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(Taller.class.getSimpleName(), tallerDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!tallerRepository.existsById(id)) {
                throw new NotFoundException(Taller.class.getSimpleName(), id);
            }
            tallerRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(Taller.class.getSimpleName(), id, ex);
        }
    }
}