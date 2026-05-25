package com.example.carcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** Municipio con código INE, pensado para poblar un Spinner en Android. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalidadDto implements Serializable {

    /** Código INE del municipio, 5 dígitos (ej: "03065"). */
    private String codigoMunicipio;

    /** Nombre oficial del municipio (ej: "Elche/Elx"). */
    private String nombre;

    /** Nombre de la provincia (ej: "Alicante"). */
    private String provincia;
}
