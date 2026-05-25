package com.example.carcare.dto;

import com.example.carcare.entity.Taller;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TallerDto implements Serializable {
    Long idTaller;

    @NotBlank(message = "El nombre del taller es obligatorio")
    String nombre;

    String direccion;
    Double latitud;
    Double longitud;
    String telefono;
    String email;
    String web;
}