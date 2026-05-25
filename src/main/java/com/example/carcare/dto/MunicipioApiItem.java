package com.example.carcare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo interno para deserializar la respuesta del endpoint MINETUR
 * /Listados/MunicipiosPorProvincia/{idProvincia}
 *
 * Ejemplo de elemento del array devuelto:
 * { "IDMunicipio":"03065","IDProvincia":"03","IDCCAA":"10",
 *   "Municipio":"Elche/Elx","Provincia":"Alicante" }
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MunicipioApiItem {

    @JsonProperty("IDMunicipio")
    private String idMunicipio;

    @JsonProperty("Municipio")
    private String nombre;

    @JsonProperty("Provincia")
    private String provincia;
}
