package com.example.carcare.service;

import com.example.carcare.dto.FacturaDto;
import com.example.carcare.entity.Factura;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.DeleteEntityException;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.exception.UpdateEntityException;
import com.example.carcare.mapper.FacturaMapper;
import com.example.carcare.repository.FacturaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FacturaService implements IFacturaService {

    private final FacturaRepository facturaRepository;
    private final FacturaMapper facturaMapper;

    @Override
    public List<FacturaDto> findAll() {
        try {
            List<Factura> facturas = (List<Factura>) facturaRepository.findAll();
            return facturaMapper.toDTOList(facturas);
        } catch (Exception ex) {
            throw new ErrorException("Error al obtener todas las facturas", ex);
        }
    }

    @Override
    public FacturaDto findById(Long id) {
        try {
            Factura factura = facturaRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(Factura.class.getSimpleName(), id));
            return facturaMapper.toDTO(factura);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar la factura con id: " + id, ex);
        }
    }

    @Override
    public Optional<FacturaDto> findByMantenimientoId(Long idMantenimiento) {
        try {
            return facturaRepository.findByMantenimientoIdMantenimiento(idMantenimiento).map(facturaMapper::toDTO);
        } catch (Exception ex) {
            throw new ErrorException("Error al buscar factura del mantenimiento con id: " + idMantenimiento, ex);
        }
    }

    @Override
    public FacturaDto save(FacturaDto facturaDto) {
        try {
            Factura factura = facturaMapper.toEntity(facturaDto);
            Factura saved = facturaRepository.save(factura);
            return facturaMapper.toDTO(saved);
        } catch (Exception ex) {
            throw new CreateEntityException(Factura.class.getSimpleName(), facturaDto, ex);
        }
    }

    @Override
    public FacturaDto update(Long id, FacturaDto facturaDto) {
        try {
            if (!facturaRepository.existsById(id)) {
                throw new NotFoundException(Factura.class.getSimpleName(), id);
            }
            Factura factura = facturaMapper.toEntity(facturaDto);
            factura.setIdFactura(id);
            Factura updated = facturaRepository.save(factura);
            return facturaMapper.toDTO(updated);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UpdateEntityException(Factura.class.getSimpleName(), facturaDto, ex);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (!facturaRepository.existsById(id)) {
                throw new NotFoundException(Factura.class.getSimpleName(), id);
            }
            facturaRepository.deleteById(id);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DeleteEntityException(Factura.class.getSimpleName(), id, ex);
        }
    }
}