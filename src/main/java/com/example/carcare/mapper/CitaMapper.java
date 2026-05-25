package com.example.carcare.mapper;

import com.example.carcare.dto.CitaDto;
import com.example.carcare.entity.Cita;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {VehiculoMapper.class, TipoMantenimientoMapper.class}
)
public interface CitaMapper {

    @Mapping(source = "vehiculo.idVehiculo", target = "idVehiculo")
    @Mapping(source = "tipoMantenimiento.idTipo", target = "idTipoMantenimiento")
    CitaDto toDTO(Cita cita);

    @Mapping(source = "idVehiculo", target = "vehiculo.idVehiculo")
    @Mapping(source = "idTipoMantenimiento", target = "tipoMantenimiento.idTipo")
    Cita toEntity(CitaDto citaDto);

    List<CitaDto> toDTOList(List<Cita> citas);

    List<Cita> toEntityList(List<CitaDto> citasDto);
}