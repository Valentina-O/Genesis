package org.valeneisa.Operaciones;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa una operación dentro del sistema.
 * Se almacena en la tabla "operaciones" y contiene información
 * como el código, nombre, costo base y estado de activación.
 */
@Entity
@Table(name = "operaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Operacion {

    /**
     * Identificador único de la operación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOperacion;

    /**
     * Código único que identifica la operación.
     */
    @Column(name = "codigo", nullable = false)
    private String codigo;

    /**
     * Nombre descriptivo de la operación.
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Costo base en tokens requerido para ejecutar la operación.
     */
    @Column(name = "costo_base")
    private Integer costoBase;

    /**
     * Indica si la operación está activa o disponible.
     */
    @Column(name = "esta_activa")
    private Boolean estaActiva;
}