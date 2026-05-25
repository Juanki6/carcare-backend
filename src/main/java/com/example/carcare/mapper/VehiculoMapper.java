package com.example.carcare.mapper;

import com.example.carcare.dto.VehiculoDto;
import com.example.carcare.entity.Vehiculo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UsuarioMapper.class}
)
public interface VehiculoMapper {

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    VehiculoDto toDTO(Vehiculo vehiculo);

    @Mapping(source = "idUsuario", target = "usuario.idUsuario")
    Vehiculo toEntity(VehiculoDto vehiculoDto);

    List<VehiculoDto> toDTOList(List<Vehiculo> vehiculos);

    List<Vehiculo> toEntityList(List<VehiculoDto> vehiculosDto);
}