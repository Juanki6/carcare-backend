package com.example.carcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tipo_mantenimiento")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TipoMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Long idTipo;

    @Column(name = "nombre_tipo", nullable = false, unique = true)
    private String nombreTipo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "intervalo_km_default")
    private Integer intervaloKmDefault;

    @Column(name = "intervalo_meses_default")
    private Integer intervaloMesesDefault;

    @OneToMany(mappedBy = "tipo", cascade = CascadeType.ALL)
    private List<Mantenimiento> mantenimientos;
}