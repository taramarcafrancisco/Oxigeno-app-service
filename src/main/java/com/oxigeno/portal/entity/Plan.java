package com.oxigeno.portal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "planes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idplan")
    private Integer idPlan;

    @Column(name = "nombre", nullable = false, unique = true, length = 120)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "duracion_dias", nullable = false)
    private Integer duracionDias;

    @Column(name = "clases_por_semana")
    private Integer clasesPorSemana;

    @Column(name = "acceso_libre", nullable = false)
    private Boolean accesoLibre = Boolean.TRUE;

    @Column(name = "activo", nullable = false)
    private Boolean activo = Boolean.TRUE;

    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UsuarioPlan> usuariosPlan;
}