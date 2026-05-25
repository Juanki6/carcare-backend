package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvolucionPreciosDTO {
    private LocalDate fecha;
    private Double precioMedio;
    private String tipo;
}
