package com.example.carcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoDto implements Serializable {
    private Long idVehiculo;
    private Long idUsuario;

    @NotBlank(message = "La matrícula es obligatoria")
    @Pattern(regexp = "^[0-9]{4}[A-Z]{3}$", message = "Formato inválido (ej: 1234ABC)")
    private String matricula;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    private LocalDate fechaMatriculacion;
    private String combustible;
    private Integer kilometraje;
    private String color;
}