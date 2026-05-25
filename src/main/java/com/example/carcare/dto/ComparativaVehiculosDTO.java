package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComparativaVehiculosDTO {
    private Long idVehiculo;
    private String nombreVehiculo;
    private Double consumoMedio;
    private Double gastoTotal;
    private Integer numRepostajes;
}