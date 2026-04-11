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

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private Plan plan;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}