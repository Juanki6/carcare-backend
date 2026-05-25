package com.example.carcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "factura")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long idFactura;

    @OneToOne
    @JoinColumn(name = "id_mantenimiento", nullable = false)
    private Mantenimiento mantenimiento;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @Column(name = "importe")
    private BigDecimal importe;

    @Column(name = "nombre_taller")
    private String nombreTaller;

    @Column(name = "ruta_imagen")
    private String rutaImagen;

    @Column(name = "ocr_texto")
    private String ocrTexto;
}