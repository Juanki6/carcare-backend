package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MantenimientoCreateDTO {
    private Long idVehiculo;
    private String tipoEnum;
    private LocalDate fecha;
    private Integer kilometraje;
    private String nombreTaller;
    private String notas;
}