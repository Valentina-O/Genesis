package org.valeneisa.Core;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa la tasa de cambio vigente en la plataforma.
 * <p>
 * Almacena el valor numérico de la tasa y la fecha en que fue actualizada
 * por última vez. Es utilizada por las operaciones de conversión de moneda
 * para aplicar el tipo de cambio correspondiente.
 * </p>
 *
 * @see ITasaCambioRepositorio
 */
@Entity
@Table(name = "tasa_cambio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TasaCambio {

    /**
     * Identificador único de la tasa de cambio, generado automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Valor numérico de la tasa de cambio. No puede ser nulo.
     */
    @Column(name = "valor", nullable = false)
    private Double valor;

    /**
     * Fecha y hora en que fue registrada o actualizada la tasa de cambio.
     */
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}