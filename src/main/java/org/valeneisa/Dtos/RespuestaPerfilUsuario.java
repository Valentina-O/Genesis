package org.valeneisa.Dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa la respuesta con la información del perfil de un usuario.
 * Incluye datos básicos, estado de la cuenta, rol y tokens disponibles.
 */
@Getter
@Setter
public class RespuestaPerfilUsuario {

    /**
     * Identificador único del usuario.
     */
    private Long idUsuario;

    /**
     * Nombre de usuario.
     */
    private String usuario;

    /**
     * Correo electrónico asociado a la cuenta.
     */
    private String correoElectronico;

    /**
     * Rol del usuario dentro del sistema.
     */
    private String rolUsuario;

    /**
     * Cantidad de tokens disponibles para el usuario.
     */
    private int tokensDisponibles;

    /**
     * Indica si el usuario está activo en el sistema.
     */
    private boolean estaActivo;

    /**
     * Nombre del plan activo del usuario.
     */
    private String planActivo;
}