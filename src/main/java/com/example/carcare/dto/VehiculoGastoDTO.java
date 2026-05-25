package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehiculoGastoDTO {
    private String marca;
    private String modelo;
    private String matricula;
    private Double gastoTotal;
}
