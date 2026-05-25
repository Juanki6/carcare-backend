package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.carcare.entity.TipoMantenimiento}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoMantenimientoDto implements Serializable {
    Long idTipo;
    String nombreTipo;
    String descripcion;
    Integer intervaloKmDefault;
    Integer intervaloMesesDefault;
}