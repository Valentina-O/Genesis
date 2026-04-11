package org.valeneisa.tokens;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.valeneisa.usuario.entidad.Usuario;
import org.valeneisa.Operaciones.Operacion;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaccion;

    @Column(name = "tokens_consumidos")
    private Integer tokensConsumidos;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_operacion")
    private Operacion operacion;
}