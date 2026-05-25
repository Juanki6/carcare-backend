package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeguroCreateDTO {
    private Long idVehiculo;
    private String compania;
    private String numeroPoliza;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String telefonoAsistencia;
    private String coberturas;
    private BigDecimal precioAnual;
    private Boolean activo;
}
