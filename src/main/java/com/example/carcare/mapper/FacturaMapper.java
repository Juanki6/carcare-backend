package com.example.carcare.mapper;

import com.example.carcare.dto.FacturaDto;
import com.example.carcare.entity.Factura;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {MantenimientoMapper.class}
)
public interface FacturaMapper {

    @Mapping(source = "mantenimiento.idMantenimiento", target = "idMantenimiento")
    FacturaDto toDTO(Factura factura);

    @Mapping(source = "idMantenimiento", target = "mantenimiento.idMantenimiento")
    Factura toEntity(FacturaDto facturaDto);

    List<FacturaDto> toDTOList(List<Factura> facturas);

    List<Factura> toEntityList(List<FacturaDto> facturasDto);
}