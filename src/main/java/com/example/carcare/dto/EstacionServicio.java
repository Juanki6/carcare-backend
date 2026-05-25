package com.example.carcare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo interno: una estación de servicio tal como la devuelve la API MINETUR.
 * Precios y coordenadas vienen como String con coma decimal ("1,599", "38,26667").
 *
 * NOTA: los @JsonProperty con caracteres no ASCII usan secuencias Unicode (uXXXX)
 * para garantizar una deserialización correcta independientemente de la codificación
 * del archivo fuente o del compilador.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstacionServicio {

    @JsonProperty("IDEESS")
    private String ideess;

    // "Rótulo"  — ó = ó, usando escape Unicode para evitar problemas de codificación de archivo
    @JsonProperty("Rótulo")
    private String rotulo;

    // "Dirección" — ó = ó
    @JsonProperty("Dirección")
    private String direccion;

    @JsonProperty("Localidad")
    private String localidad;

    @JsonProperty("Municipio")
    private String municipio;

    @JsonProperty("Provincia")
    private String provincia;

    @JsonProperty("C.P.")
    private String codigoPostal;

    @JsonProperty("Horario")
    private String horario;

    // Coordenadas como String con coma decimal, ej: "38,26667"
    @JsonProperty("Latitud")
    private String latitud;

    // La API usa este nombre exacto con paréntesis: "Longitud (WGS84)"
    @JsonProperty("Longitud (WGS84)")
    private String longitud;

    // Precios como String con coma decimal, ej: "1,599". Vacío si no se vende.
    @JsonProperty("Precio Gasolina 95 E5")
    private String precioGasolina95;

    @JsonProperty("Precio Gasolina 98 E5")
    private String precioGasolina98;

    @JsonProperty("Precio Gasoleo A")
    private String precioGasoleoA;

    @JsonProperty("Precio Gasoleo B")
    private String precioGasoleoB;

    @JsonProperty("Precio Gasoleo Premium")
    private String precioGasoleoPremium;

    // "Precio Gases licuados del petróleo"
    @JsonProperty("Precio Gases licuados del petróleo")
    private String precioGlp;

    @JsonProperty("Precio Gas Natural Comprimido")
    private String precioGnc;
}
