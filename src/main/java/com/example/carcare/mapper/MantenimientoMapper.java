package com.example.carcare.mapper;

import com.example.carcare.dto.MantenimientoDto;
import com.example.carcare.entity.Mantenimiento;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {VehiculoMapper.class, TipoMantenimientoMapper.class, TallerMapper.class}
)
public interface MantenimientoMapper {

    @Mapping(source = "vehiculo.idVehiculo", target = "idVehiculo")
    @Mapping(source = "tipo.idTipo", target = "idTipo")
    @Mapping(source = "taller.idTaller", target = "idTaller")
    MantenimientoDto toDTO(Mantenimiento mantenimiento);

    @Mapping(source = "idVehiculo", target = "vehiculo.idVehiculo")
    @Mapping(source = "idTipo", target = "tipo.idTipo")
    @Mapping(source = "idTaller", target = "taller.idTaller")
    Mantenimiento toEntity(MantenimientoDto mantenimientoDto);

    // Métodos para listas
    List<MantenimientoDto> toDTOList(List<Mantenimiento> mantenimientos);

    List<Mantenimiento> toEntityList(List<MantenimientoDto> mantenimientosDto);
}