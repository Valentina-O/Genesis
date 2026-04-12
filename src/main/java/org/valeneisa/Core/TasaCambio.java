package org.valeneisa.Core;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasa_cambio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TasaCambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}