package com.example.carcare.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MantenimientoDto {
    private Long idMantenimiento;
    private Long idVehiculo;
    private Long idTipo;
    private Long idTaller;
    private String tipoEnum;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    private Integer kilometraje;
    private String notas;
    private String nombreTaller;
}