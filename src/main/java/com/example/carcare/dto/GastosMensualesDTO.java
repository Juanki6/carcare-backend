package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GastosMensualesDTO {
    private String mes;
    private Double gastoCombustible;
    private Double gastoMantenimiento;
    private Double total;
}
