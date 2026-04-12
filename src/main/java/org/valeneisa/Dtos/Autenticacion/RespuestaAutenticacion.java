package org.valeneisa.Dtos.Autenticacion;

import lombok.Getter;

@Getter
public class RespuestaAutenticacion {

    private String token;
    private String tipo = "Bearer";

    public RespuestaAutenticacion(String token) {
        this.token = token;
    }

}