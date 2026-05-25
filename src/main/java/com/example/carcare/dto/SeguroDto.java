package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeguroDto {
    private Long idSeguro;
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
