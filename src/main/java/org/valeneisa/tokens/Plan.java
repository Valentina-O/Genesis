package org.valeneisa.tokens;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "planes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlan;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tokens_otorgados", nullable = false)
    private Integer tokensOtorgados;

    @Column(name = "esta_activo")
    private Boolean estaActivo;

    @OneToMany(mappedBy = "plan")
    private List<Suscripcion> suscripciones;
}