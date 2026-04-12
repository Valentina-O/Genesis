package org.valeneisa.Operaciones;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "operaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOperacion;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "costo_base")
    private Integer costoBase;

    @Column(name = "esta_activa")
    private Boolean estaActiva;
}