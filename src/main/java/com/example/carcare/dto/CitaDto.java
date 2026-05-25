package com.example.carcare.dto;

import com.example.carcare.entity.Cita;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Cita}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitaDto implements Serializable {
    Long idCita;
    Long idVehiculo;
    Long idTipoMantenimiento;
    String titulo;
    LocalDate fechaProgramada;
    Integer kilometrajeProgramado;
    String notas;
    Boolean completada;
    Boolean recordatorioEnviado;
}