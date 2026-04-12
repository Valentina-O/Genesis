package org.valeneisa.Dtos.autenticación;

public class RespuestaAutenticacion {

    private String token;
    private String tipo = "Bearer";

    public RespuestaAutenticacion(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }
}