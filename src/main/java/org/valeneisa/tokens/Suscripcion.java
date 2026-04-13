package org.valeneisa.tokens;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.valeneisa.usuario.entidad.Usuario;

/**
 * Entidad que representa la suscripción de un usuario a un plan.
 * Contiene información sobre el estado, la fecha de inicio
 * y las relaciones con el usuario y el plan.
 */
@Entity
@Table(name = "suscripciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Suscripcion {

    /**
     * Identificador único de la suscripción.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSuscripcion;

    /**
     * Fecha de inicio de la suscripción.
     */
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    /**
     * Indica si la suscripción está activa.
     */
    @Column(name = "esta_activa")
    private Boolean estaActiva;

    /**
     * Plan asociado a la suscripción.
     * Un plan puede estar en múltiples suscripciones.
     */
    @ManyToOne // Un plan puede estar en muchas suscripciones
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;

    /**
     * Usuario asociado a la suscripción.
     * Un usuario tiene una única suscripción activa.
     */
    @OneToOne // Un usuario tiene una única suscripción
    @JoinColumn(name = "id_usuario", unique = true, nullable = false)
    private Usuario usuario;
}