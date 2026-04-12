package org.valeneisa.Dtos.Autenticacion;

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