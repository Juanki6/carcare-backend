package com.example.carcare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Envoltorio de la respuesta de la API del Gobierno de España.
 * Estructura: { "Fecha": "...", "ListaEESSPrecio": [...], "ResultadoConsulta": "OK" }
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GasolineraApiResponse {

    @JsonProperty("Fecha")
    private String fecha;

    @JsonProperty("ListaEESSPrecio")
    private List<EstacionServicio> listaEstaciones;

    @JsonProperty("ResultadoConsulta")
    private String resultadoConsulta;
}
