package com.example.carcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "seguro")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Seguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguro")
    private Long idSeguro;

    @OneToOne
    @JoinColumn(name = "id_vehiculo", nullable = false, unique = true)
    private Vehiculo vehiculo;

    @Column(name = "compania", nullable = false, length = 120)
    private String compania;

    @Column(name = "numero_poliza", nullable = false, unique = true, length = 60)
    private String numeroPoliza;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "telefono_asistencia", length = 20)
    private String telefonoAsistencia;

    @Column(name = "coberturas", columnDefinition = "TEXT")
    private String coberturas;

    @Column(name = "precio_anual", precision = 10, scale = 2)
    private BigDecimal precioAnual;

    @Column(name = "activo")
    private Boolean activo = true;
}
