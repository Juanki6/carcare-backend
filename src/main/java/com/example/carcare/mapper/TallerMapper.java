package com.example.carcare.mapper;

import com.example.carcare.dto.TallerDto;
import com.example.carcare.entity.Taller;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TallerMapper {

    TallerDto toDTO(Taller taller);

    Taller toEntity(TallerDto tallerDto);

    List<TallerDto> toDTOList(List<Taller> talleres);

    List<Taller> toEntityList(List<TallerDto> talleresDto);

}
