package com.example.carcare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "mantenimiento")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Long idMantenimiento;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoMantenimiento tipo;

    @ManyToOne
    @JoinColumn(name = "id_taller")
    private Taller taller;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_enum")
    private TipoMantenimientoEnum tipoEnum;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "kilometraje", nullable = false)
    private Integer kilometraje;

    @Column(name = "notas")
    private String notas;

    // NUEVO CAMPO
    @Column(name = "nombre_taller")
    private String nombreTaller;

    @OneToOne(mappedBy = "mantenimiento", cascade = CascadeType.ALL)
    private Factura factura;

    public enum TipoMantenimientoEnum {
        ITV, ACEITE, NEUMATICOS, CORREA_DISTRIBUCION, FRENOS, BATERIA, OTRO
    }
}