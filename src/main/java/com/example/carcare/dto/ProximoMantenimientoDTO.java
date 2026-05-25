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
public class ProximoMantenimientoDTO {
    private Long idVehiculo;
    private String tipo;
    private LocalDate fechaEstimada;
    private Integer kilometrajeEstimado;
    private Integer kmRestante;
    private Integer diasRestantes;
    private boolean esUrgente;
}
