package org.valeneisa.tokens;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.valeneisa.usuario.entidad.Usuario;

@Entity
@Table(name = "suscripciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSuscripcion;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "esta_activa")
    private Boolean estaActiva;

    @ManyToOne // Un plan puede estar en muchas suscripciones [cite: 27]
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;

    @OneToOne // Un usuario tiene una única suscripción
    @JoinColumn(name = "id_usuario", unique = true, nullable = false)
    private Usuario usuario;
}