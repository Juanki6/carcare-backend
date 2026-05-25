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
public class EstadisticasDTO {
    private Double consumoMedioL100km;
    private Double precioMedioLitro;
    private Double gastoTotal;
    private LocalDate ultimoRepostaje;
    private Integer kilometrajeTotal;
    private Integer numRepostajes;
}