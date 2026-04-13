package org.valeneisa.tokens;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Entidad que representa un plan de suscripción dentro del sistema.
 * Define la cantidad de tokens otorgados y su estado de disponibilidad.
 */
@Entity
@Table(name = "planes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    /**
     * Identificador único del plan.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlan;

    /**
     * Nombre del plan.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Cantidad de tokens otorgados por el plan.
     */
    @Column(name = "tokens_otorgados", nullable = false)
    private Integer tokensOtorgados;

    /**
     * Indica si el plan está activo.
     */
    @Column(name = "esta_activo")
    private Boolean estaActivo;

    /**
     * Lista de suscripciones asociadas a este plan.
     */
    @OneToMany(mappedBy = "plan")
    private List<Suscripcion> suscripciones;
}