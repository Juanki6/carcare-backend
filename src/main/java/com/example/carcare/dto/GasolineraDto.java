package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GasolineraDto implements Serializable {

    private String id;
    private String nombre;
    private String direccion;
    private String localidad;
    private String provincia;
    private String codigoPostal;
    private String horario;
    private Double latitud;
    private Double longitud;

    // Precios en €/L (null si la estación no vende ese combustible)
    private Double precioGasolina95;
    private Double precioGasolina98;
    private Double precioGasoleoA;
    private Double precioGasoleoB;
}
