package com.example.carcare.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepostajeDto implements Serializable {
    Long idRepostaje;
    Long idVehiculo;
    LocalDate fecha;
    Integer kilometraje;

    @NotNull(message = "Los litros son obligatorios")
    @Positive(message = "Los litros deben ser un valor positivo")
    BigDecimal litros;

    @NotNull(message = "El precio por litro es obligatorio")
    @Positive(message = "El precio por litro debe ser un valor positivo")
    BigDecimal precioLitro;

    String gasolinera;
}