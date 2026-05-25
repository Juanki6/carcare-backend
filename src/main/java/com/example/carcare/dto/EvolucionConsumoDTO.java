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
public class EvolucionConsumoDTO {
    private LocalDate fecha;
    private Double consumoL100km;
    private Double precioLitro;
    private Double litros;
    private Integer kilometraje;
}