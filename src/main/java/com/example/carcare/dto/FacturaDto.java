package com.example.carcare.dto;

import com.example.carcare.entity.Factura;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link Factura}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaDto implements Serializable {
    Long idFactura;
    Long idMantenimiento;
    LocalDate fechaEmision;
    BigDecimal importe;
    String nombreTaller;
    String rutaImagen;
    String ocrTexto;
}