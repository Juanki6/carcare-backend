package com.example.carcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cita")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long idCita;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "id_tipo_mantenimiento")
    private TipoMantenimiento tipoMantenimiento;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "fecha_programada", nullable = false)
    private LocalDate fechaProgramada;

    @Column(name = "kilometraje_programado")
    private Integer kilometrajeProgramado;

    @Column(name = "notas")
    private String notas;

    @Column(name = "completada")
    private Boolean completada = false;

    @Column(name = "recordatorio_enviado")
    private Boolean recordatorioEnviado = false;
}