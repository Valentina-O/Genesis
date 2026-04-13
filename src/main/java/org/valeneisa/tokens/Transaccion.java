package org.valeneisa.tokens;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.Operaciones.Operacion;

/**
 * Entidad que representa una transacción de consumo de tokens.
 * Registra qué usuario realizó una operación, cuántos tokens consumió
 * y en qué momento se llevó a cabo.
 */
@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion {

    /**
     * Identificador único de la transacción.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaccion;

    /**
     * Cantidad de tokens consumidos en la operación.
     */
    @Column(name = "tokens_consumidos")
    private Integer tokensConsumidos;

    /**
     * Fecha en la que se realizó la transacción.
     */
    @Column(name = "fecha")
    private LocalDateTime fecha;

    /**
     * Usuario que realizó la operación.
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    /**
     * Operación ejecutada en la transacción.
     */
    @ManyToOne
    @JoinColumn(name = "id_operacion")
    private Operacion operacion;
}