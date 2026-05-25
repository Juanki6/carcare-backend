package com.example.carcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repostaje")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Repostaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repostaje")
    private Long idRepostaje;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "kilometraje", nullable = false)
    private Integer kilometraje;

    @Column(name = "litros", nullable = false)
    private BigDecimal litros;

    @Column(name = "precio_litro", nullable = false)
    private BigDecimal precioLitro;

    @Column(name = "gasolinera")
    private String gasolinera;
}