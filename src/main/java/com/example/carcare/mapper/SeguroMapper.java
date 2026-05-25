package com.example.carcare.mapper;

import com.example.carcare.dto.SeguroDto;
import com.example.carcare.entity.Seguro;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {VehiculoMapper.class}
)
public interface SeguroMapper {

    @Mapping(source = "vehiculo.idVehiculo", target = "idVehiculo")
    SeguroDto toDTO(Seguro seguro);

    @Mapping(source = "idVehiculo", target = "vehiculo.idVehiculo")
    Seguro toEntity(SeguroDto seguroDto);

    List<SeguroDto> toDTOList(List<Seguro> seguros);

    List<Seguro> toEntityList(List<SeguroDto> segurosDto);
}