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
public class CitaCreateDTO {
    private Long idVehiculo;
    private Long idTipoMantenimiento;
    private String titulo;
    private LocalDate fechaProgramada;
    private Integer kilometrajeProgramado;
    private String notas;
    private Boolean completada;
    private Boolean recordatorioEnviado;
}
