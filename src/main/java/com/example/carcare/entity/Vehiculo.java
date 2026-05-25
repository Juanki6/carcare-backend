package com.example.carcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "vehiculo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "matricula", nullable = false)
    private String matricula;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "fecha_matriculacion")
    private LocalDate fechaMatriculacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "combustible", nullable = false)
    private Combustible combustible;

    @Column(name = "kilometraje", nullable = false)
    private Integer kilometraje;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<Mantenimiento> mantenimientos;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<Repostaje> repostajes;

    public enum Combustible {
        GASOLINA, DIESEL, HIBRIDO, ELECTRICO, GLP, GNC, OTRO
    }
}