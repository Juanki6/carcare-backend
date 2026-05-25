package com.example.carcare.mapper;

import com.example.carcare.dto.TipoMantenimientoDto;
import com.example.carcare.entity.TipoMantenimiento;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TipoMantenimientoMapper {

    TipoMantenimientoDto toDTO(TipoMantenimiento tipoMantenimiento);

    TipoMantenimiento toEntity(TipoMantenimientoDto tipoMantenimientoDto);

    List<TipoMantenimientoDto> toDTOList(List<TipoMantenimiento> tipos);

    List<TipoMantenimiento> toEntityList(List<TipoMantenimientoDto> tiposDto);
}