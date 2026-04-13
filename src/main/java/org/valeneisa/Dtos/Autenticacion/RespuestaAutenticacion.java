package org.valeneisa.Dtos.Autenticacion;

import lombok.Getter;

/**
 * DTO que representa la respuesta de autenticación.
 * Contiene el token JWT generado y el tipo de autorización.
 */
@Getter
public class RespuestaAutenticacion {

    /**
     * Token JWT generado para el usuario autenticado.
     */
    private String token;

    /**
     * Tipo de autenticación utilizado (por defecto "Bearer").
     */
    private String tipo = "Bearer";

    /**
     * Constructor que inicializa la respuesta con el token generado.
     *
     * @param token Token JWT.
     */
    public RespuestaAutenticacion(String token) {
        this.token = token;
    }

}