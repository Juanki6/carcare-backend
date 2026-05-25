package com.example.carcare.mapper;

import com.example.carcare.dto.UsuarioDto;
import com.example.carcare.entity.Usuario;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UsuarioMapper {

    UsuarioDto toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDto usuarioDto);

    List<UsuarioDto> toDTOList(List<Usuario> usuarios);

    List<Usuario> toEntityList(List<UsuarioDto> usuariosDto);
}