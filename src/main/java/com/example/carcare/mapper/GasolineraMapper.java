package com.example.carcare.mapper;

import com.example.carcare.dto.EstacionServicio;
import com.example.carcare.dto.GasolineraDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface GasolineraMapper {

    @Mapping(source = "ideess",  target = "id")
    @Mapping(source = "rotulo",  target = "nombre")
    @Mapping(expression = "java(parsearDecimal(e.getLatitud()))",         target = "latitud")
    @Mapping(expression = "java(parsearDecimal(e.getLongitud()))",        target = "longitud")
    @Mapping(expression = "java(parsearDecimal(e.getPrecioGasolina95()))", target = "precioGasolina95")
    @Mapping(expression = "java(parsearDecimal(e.getPrecioGasolina98()))", target = "precioGasolina98")
    @Mapping(expression = "java(parsearDecimal(e.getPrecioGasoleoA()))",  target = "precioGasoleoA")
    @Mapping(expression = "java(parsearDecimal(e.getPrecioGasoleoB()))",  target = "precioGasoleoB")
    GasolineraDto toDTO(EstacionServicio e);

    List<GasolineraDto> toDTOList(List<EstacionServicio> estaciones);

    /** Convierte "1,599" → 1.599 y "" / null → null */
    default Double parsearDecimal(String valor) {
        if (valor == null || valor.isBlank()) return null;
        try {
            return Double.parseDouble(valor.replace(",", ".").trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
