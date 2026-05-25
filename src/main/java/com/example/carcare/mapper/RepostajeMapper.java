package com.example.carcare.mapper;

import com.example.carcare.dto.RepostajeDto;
import com.example.carcare.entity.Repostaje;
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
public interface RepostajeMapper {

    @Mapping(source = "vehiculo.idVehiculo", target = "idVehiculo")
    RepostajeDto toDTO(Repostaje repostaje);

    @Mapping(source = "idVehiculo", target = "vehiculo.idVehiculo")
    Repostaje toEntity(RepostajeDto repostajeDto);

    List<RepostajeDto> toDTOList(List<Repostaje> repostajes);

    List<Repostaje> toEntityList(List<RepostajeDto> repostajesDto);
}