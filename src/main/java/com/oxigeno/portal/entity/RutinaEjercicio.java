package com.oxigeno.portal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "rutina_ejercicios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RutinaEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina_ejercicio")
    private Integer idRutinaEjercicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rutina", nullable = false)
    @JsonIgnore
    private Rutina rutina;

    @Column(name = "ejercicio", nullable = false, length = 120)
    private String ejercicio;

    @Column(name = "series")
    private Integer series;

    @Column(name = "repeticiones", length = 50)
    private String repeticiones;

    @Column(name = "descanso", length = 50)
    private String descanso;

    @Column(name = "orden")
    private Integer orden;
}