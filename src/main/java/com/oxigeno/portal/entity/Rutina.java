package com.oxigeno.portal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rutinas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina")
    private Integer idRutina;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "objetivo", length = 250)
    private String objetivo;

    @Column(name = "nivel", length = 50)
    private String nivel;

    @Column(name = "dias", length = 100)
    private String dias;

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion;

    @Column(name = "activa", nullable = false)
    private Boolean activa = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({
            "password",
            "roles",
            "hibernateLazyInitializer",
            "handler"
    })
    private Usuario usuario;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    @Builder.Default
    private List<RutinaEjercicio> ejercicios = new ArrayList<>();
}