package org.valeneisa.Dtos.autenticacion;

import lombok.Getter;

@Getter
public class RespuestaAutenticacion {

    private String token;
    private String tipo = "Bearer";

    public RespuestaAutenticacion(String token) {
        this.token = token;
    }

}