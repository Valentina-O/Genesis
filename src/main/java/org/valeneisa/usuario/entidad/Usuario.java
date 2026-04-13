package org.valeneisa.usuario.entidad;

import jakarta.persistence.*;
import lombok.*;
import org.valeneisa.tokens.Suscripcion;

/**
 * Entidad que representa a un usuario dentro del sistema.
 * Contiene información personal, credenciales, rol,
 * estado de la cuenta y relación con su suscripción.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    /**
     * Nombre de usuario único.
     */
    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    /**
     * Correo electrónico único del usuario.
     */
    @Column(name = "correoElectronico", nullable = false, unique = true)
    private String correoElectronico;

    /**
     * Contraseña encriptada del usuario.
     */
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    /**
     * Rol del usuario dentro del sistema.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Rol rolUsuario;

    /**
     * Suscripción asociada al usuario.
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Suscripcion suscripcion;

    /**
     * Cantidad de tokens disponibles del usuario.
     */
    @Column(name = "tokens_disponibles")
    private Integer tokensDisponibles;

    /**
     * Indica si el usuario está activo en el sistema.
     */
    @Column(name = "esta_activo")
    private Boolean estaActivo;
}