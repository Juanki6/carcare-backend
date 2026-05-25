package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsumoMedioGlobalDTO {
    private String marca;
    private String modelo;
    private Double consumoMedio;
}
